package com.example;

import org.apache.kafka.clients.producer.ProducerRecord;

public class RepeatProducer extends AbstractProducer {

    public static void main(String[] args) {
        for (int i = 0; i < 500; i++) {
            ProducerRecord<String, String> pRecord = new ProducerRecord<>(TOPIC_NAME, "random message: " + i);
            kafkaProducer.send(pRecord);
        }
        kafkaProducer.flush();
        kafkaProducer.close();
    }
}
