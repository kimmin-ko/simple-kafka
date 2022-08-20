package com.example.springkafkaproducer.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SpringKafkaTemplateWorker implements CommandLineRunner {

    private static final String TOPIC_NAME = "test";

//    private final KafkaTemplate<Integer, String> template;

    @Override
    public void run(String... args) {
        for (int i = 0; i < 10; i++) {
//            template.send(TOPIC_NAME, "test" + i);
        }
        System.exit(0);
    }
}
