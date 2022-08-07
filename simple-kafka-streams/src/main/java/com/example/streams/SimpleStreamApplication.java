package com.example.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

public class SimpleStreamApplication {

    private static final String APPLICATION_NAME = "streams-application";
    private static final String BOOTSTRAP_SERVERS = "my-kafka:9092";
    private static final String STREAM_LOG = "stream_log";
    private static final String STREAM_LOG_COPY = "stream_log_copy";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        builder.stream(STREAM_LOG)
                .to(STREAM_LOG_COPY);

        Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();
    }
}
