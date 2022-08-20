package com.example.springkafkaproducer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class BatchKafkaRecordListener {

    @KafkaListener(topics = "test", groupId = "test-group-01")
    public void batchListener(ConsumerRecords<String, String> records) {
        records.forEach(record -> log.info(record.toString()));
    }

    @KafkaListener(topics = "test", groupId = "test-group-02")
    public void batchListener(List<String> recordValues) {
        recordValues.forEach(log::info);
    }

    @KafkaListener(topics = "test", groupId = "test-group-03", concurrency = "3")
    public void concurrentBatchLiistener(ConsumerRecords<String, String> records) {
        records.forEach(record -> log.info(record.toString()));
    }


}
