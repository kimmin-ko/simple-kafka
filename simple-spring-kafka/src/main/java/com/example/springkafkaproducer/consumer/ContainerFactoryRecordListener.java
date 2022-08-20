package com.example.springkafkaproducer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ContainerFactoryRecordListener {

    @KafkaListener(topics = "test", groupId = "test-group", containerFactory = "customContainerFactory")
    public void customListener(String data) {
        log.info(data);
    }
}
