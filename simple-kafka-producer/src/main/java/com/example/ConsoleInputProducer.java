package com.example;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ConsoleInputProducer extends AbstractProducer {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        while (true) {
            System.out.print("input: ");
            String inputMessage = scanner.nextLine();
            if (inputMessage.equalsIgnoreCase("q")) {
                kafkaProducer.close();
                break;
            }

            ProducerRecord<String, String> pRecord = new ProducerRecord<>(TOPIC_NAME, inputMessage);

            RecordMetadata metadata = kafkaProducer.send(pRecord).get();
//            logger.info("metadata: {}", metadata.toString());
//            logger.info("pRecord: {}", pRecord);

            kafkaProducer.flush();
        }

    }

}
