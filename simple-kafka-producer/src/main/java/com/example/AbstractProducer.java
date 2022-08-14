package com.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public abstract class AbstractProducer {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractProducer.class);

    private static final String BOOTSTRAP_SERVERS = "my-kafka:9092";

    protected static final String TOPIC_NAME = "test";
    protected static final Properties configs = new Properties();
    protected static final KafkaProducer<String, String> kafkaProducer;

    static {
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        kafkaProducer = new KafkaProducer<>(configs);
    }

}
