package com.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadAbstractConsumer extends AbstractConsumer {

    public static void main(String[] args) {
        kafkaConsumer.subscribe(List.of(TOPIC_NAME));
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(10));
            for (ConsumerRecord<String, String> cRecord : records) {
                ConsumerWorker worker = new ConsumerWorker(cRecord.value());
                executorService.execute(worker);
            }
        }
    }
}
