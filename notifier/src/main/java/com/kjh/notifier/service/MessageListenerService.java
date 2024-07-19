package com.kjh.notifier.service;

import com.kjh.core.dto.MailMessage;
import com.kjh.core.service.Queue;
import com.kjh.core.util.JsonUtils;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerService {

    private final MailServiceImpl mailServiceImpl;

    public MessageListenerService(MailServiceImpl mailServiceImpl) {
        this.mailServiceImpl = mailServiceImpl;
    }

    @SqsListener(Queue.MAIL)
    public void receive(String message) {
        MailMessage mailMessage = JsonUtils.fromJson(message, MailMessage.class);
        mailServiceImpl.sendAsync(mailMessage);
    }
}
