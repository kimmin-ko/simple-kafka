package com.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.Collections;

public class ConsumerWithExactPartition extends Consumer {

    public static void main(String[] args) {
        consumer.assign(Collections.singleton(new TopicPartition(TOPIC_NAME, PARTITION_NUMBER)));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> record : records) {
                    logger.info("record: {}", record);
                }
            }
        } catch (WakeupException e) {
            logger.warn("Wakeup consumer.");
        } finally {
            consumer.close();
        }
    }
}
