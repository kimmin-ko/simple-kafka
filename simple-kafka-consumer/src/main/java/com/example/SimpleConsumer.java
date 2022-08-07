package com.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SimpleConsumer extends Consumer {

    public static void main(String[] args) {
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        while (true) {
            // 기본 옵션은 poll() 메서드가 수행될 때 일정 간격 마다 오프셋을 커밋하도록 enable.auto.commit=true로 설정되어 있다.
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();

            for (ConsumerRecord<String, String> record : records) {
                logger.info("record: {}", record);
                currentOffset.put(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1, null)
                );
                consumer.commitSync(currentOffset);
            }
            // record 처리 이후 오프셋 커밋 실행
//                consumer.commitSync();
        }
    }
}
