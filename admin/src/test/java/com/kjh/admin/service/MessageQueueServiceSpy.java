package com.kjh.admin.service;

import com.kjh.core.service.MessageQueueService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Profile("test")
@Service
public class MessageQueueServiceSpy implements MessageQueueService {

    public record Record(String queueName, Object object) {}

    private final List<Record> records = new ArrayList<>();

    @Override
    public void send(String queueName, Object object) {
        records.add(new Record(queueName, object));
    }

    public Iterable<Record> getRecords() {
        return records;
    }
}
