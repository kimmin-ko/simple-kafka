package com.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;

public class ConsumerWithAsyncCommit extends Consumer {

    public static void main(String[] args) {
        consumer.subscribe(List.of(TOPIC_NAME));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("record: {}", record);
            }
            consumer.commitAsync((offsets, e) -> {
                if (e != null) {
                    logger.error("Commit failed for offsets {}.", offsets, e);
                    return;
                }

                logger.info("Commit succeeded.");
            });
        }
    }
}
