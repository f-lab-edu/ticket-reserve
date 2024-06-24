package com.kjh.admin.service;

import com.kjh.core.service.MessageQueueService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class TestMessageQueueServiceImpl implements MessageQueueService {

    @Override
    public void send(String queueName, Object object) {
    }
}
