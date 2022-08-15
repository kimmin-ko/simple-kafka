package com.exmaple;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadConsumer extends AbstractConsumer {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            ConsumerWorker worker = new ConsumerWorker(configs, TOPIC_NAME, i);
            executorService.execute(worker);
        }
    }

}
