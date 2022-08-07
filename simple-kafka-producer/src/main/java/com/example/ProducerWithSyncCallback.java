package com.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerWithSyncCallback {

    private static final Logger logger = LoggerFactory.getLogger(ProducerWithSyncCallback.class);
    private static final String TOPIC_NAME = "test";
    private static final String BOOTSTRAP_SERVERS = "my-kafka:9092";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

        String messageValue = "testMessage11";
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, messageValue);
        RecordMetadata metadata = producer.send(record).get();
        logger.info("metadata: {}", metadata.toString());
        logger.info("record: {}", record);
        producer.flush();
        producer.close();
    }
}
