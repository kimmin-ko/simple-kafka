package com.exmaple;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public abstract class AbstractConsumer {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractConsumer.class);

    protected static final String TOPIC_NAME = "test";
    protected static final String BOOTSTRAP_SERVERS = "my-kafka:9092";
    protected static final String GROUP_ID = "test-group";
    protected static final int CONSUMER_COUNT = 3;

    protected static final Properties configs = new Properties();

    static {
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    }

    protected AbstractConsumer() {
    }
}