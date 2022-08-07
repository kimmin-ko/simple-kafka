package com.example;

import com.example.CustomPartitioner;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCustomPartitioner {

    private static final Logger logger = LoggerFactory.getLogger(com.example.SimpleProducer.class);
    private static final String TOPIC_NAME = "test";
    private static final String BOOTSTRAP_SERVERS = "my-kafka:9092";

    public static void main(String[] args) {
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class);

        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

        int partitionNo = 0;
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, partitionNo, "Pangyo", "23");
        producer.send(record);
        logger.info("{}", record);
        producer.flush();
        producer.close();
    }
}
