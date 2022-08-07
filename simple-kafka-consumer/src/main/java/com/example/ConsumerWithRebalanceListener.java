package com.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

public class ConsumerWithRebalanceListener extends Consumer {

    public static void main(String[] args) {
        consumer.subscribe(List.of(TOPIC_NAME), new RebalanceListener());

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("record: {}", record);
                currentOffset.put(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1, null)
                );
            }
            consumer.commitSync(currentOffset);
        }
    }

    private static class RebalanceListener implements ConsumerRebalanceListener {

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            logger.warn("Partitions are assigned.");
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            logger.warn("Partitions are revoked.");
            consumer.commitSync(currentOffset);
        }
    }
}
