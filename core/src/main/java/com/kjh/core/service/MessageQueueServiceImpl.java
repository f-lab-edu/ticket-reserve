package com.kjh.core.service;

import com.kjh.core.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Profile("dev")
@Service
public class MessageQueueServiceImpl implements MessageQueueService {

    @Value("${aws.sqs.region}")
    private String region;

    @Value("${aws.sqs.access-key-id}")
    private String accessKeyId;

    @Value("${aws.sqs.secret-access-key}")
    private String secretAccessKey;

    @Override
    public void send(String queueName, Object object) {
        SqsClient client = getClientBuilder().build();

        try {
            sendMessage(client, queueName, JsonUtils.toJson(object));
            client.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SqsClientBuilder getClientBuilder() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        return SqsClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials));
    }

    private String getQueueUrl(SqsClient client, String queueName) {
        GetQueueUrlRequest request = GetQueueUrlRequest.builder().queueName(queueName).build();
        return client.getQueueUrl(request).queueUrl();
    }

    private void sendMessage(SqsClient client, String queueName, String message) {
        try {
            String queueUrl = getQueueUrl(client, queueName);
            SendMessageRequest msgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();

            client.sendMessage(msgRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
