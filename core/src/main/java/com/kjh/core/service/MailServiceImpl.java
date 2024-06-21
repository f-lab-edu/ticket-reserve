package com.kjh.core.service;

import com.kjh.core.template.TemplateCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.SesClientBuilder;
import software.amazon.awssdk.services.ses.model.*;

import java.util.Map;

@Profile("dev")
@Service
public class MailServiceImpl implements MailService {

    private final TemplateEngine mailTemplateEngine;

    @Value("${aws.ses.access-key-id}")
    private String accessKeyId;

    @Value("${aws.ses.secret-access-key}")
    private String secretAccessKey;

    @Value("${mail.sender}")
    private String sender;

    public MailServiceImpl(TemplateEngine mailTemplateEngine) {
        this.mailTemplateEngine = mailTemplateEngine;
    }

    @Override
    @Async
    public void sendAsync(TemplateCode templateCode, String email, Map<String, Object> model) {
        SesClient client = getClientBuilder().build();

        try {
            Context context = new Context();
            context.setVariables(model);
            String bodyHTML = mailTemplateEngine.process(templateCode.getFilename(), context);

            sendEmail(client, sender, email, templateCode.getSubject(), bodyHTML);
            client.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SesClientBuilder getClientBuilder() {
        Region region = Region.AP_NORTHEAST_2;
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        return SesClient.builder()
            .region(region)
            .credentialsProvider(StaticCredentialsProvider.create(credentials));
    }

    private void sendEmail(SesClient client, String sender, String recipient, String subject, String bodyHTML) {
        Destination destination = Destination.builder().toAddresses(recipient).build();
        Content content = Content.builder().data(bodyHTML).build();
        Content sub = Content.builder().data(subject).build();
        Body body = Body.builder().html(content).build();
        Message msg = Message.builder().subject(sub).body(body).build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
            .destination(destination)
            .message(msg)
            .source(sender)
            .build();

        try {
            client.sendEmail(emailRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
