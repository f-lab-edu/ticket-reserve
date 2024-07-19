package com.kjh.core.service;

public interface MessageQueueService {

    void send(String queueName, Object object);
}
