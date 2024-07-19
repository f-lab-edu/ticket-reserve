package com.kjh.notifier.service;

import com.kjh.core.dto.MailMessage;
import org.springframework.beans.factory.annotation.Value;
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

@Service
public class MailServiceImpl implements MailService {

    private final TemplateEngine mailTemplateEngine;

    @Value("${aws.ses.region}")
    private String region;

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
    public void sendAsync(MailMessage mailMessage) {
        SesClient client = getClientBuilder().build();

        try {
            Context context = new Context();
            context.setVariables(mailMessage.model());
            String bodyHTML = mailTemplateEngine.process(mailMessage.templateCode().getFilename(), context);

            sendEmail(client, sender, mailMessage.email(), mailMessage.templateCode().getSubject(), bodyHTML);
            client.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SesClientBuilder getClientBuilder() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        return SesClient.builder()
            .region(Region.of(region))
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
