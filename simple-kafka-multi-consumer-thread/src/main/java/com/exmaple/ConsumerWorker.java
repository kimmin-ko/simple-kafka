package com.exmaple;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ConsumerWorker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerWorker.class);

    private Properties props;
    private String topic;
    private String threadName;
    private KafkaConsumer<String, String> kafkaConsumer;

    public ConsumerWorker(Properties props, String topic, int number) {
        this.props = props;
        this.topic = topic;
        this.threadName = "consumer-thread-" + number;
    }

    @Override
    public void run() {
        this.kafkaConsumer = new KafkaConsumer<>(props);
        this.kafkaConsumer.subscribe(List.of(topic));
        while (true) {
            ConsumerRecords<String, String> records = this.kafkaConsumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("{}, {}", this.threadName, record);
            }
        }
    }

}
