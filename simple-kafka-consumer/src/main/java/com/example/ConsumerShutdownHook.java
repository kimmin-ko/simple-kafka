package com.example;

public class ConsumerShutdownHook extends Consumer {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());


    }

    static class ShutdownThread extends Thread {
        @Override
        public void run() {
            logger.info("Shutdown hook.");
        }
    }
}
