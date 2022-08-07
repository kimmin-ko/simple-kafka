package com.example;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.config.ConfigResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class KafkaAdminClient {

    private static final Logger logger = LoggerFactory.getLogger(KafkaAdminClient.class);

    public static void main(String[] args) throws Exception {
        Properties configs = new Properties();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "my-kafka:9092");
        try (AdminClient admin = AdminClient.create(configs)) {
            logger.info("== Get broker information.");
            for (Node node : admin.describeCluster().nodes().get()) {
                logger.info("node: {}", node);

                ConfigResource cr = new ConfigResource(ConfigResource.Type.BROKER, node.idString());
                DescribeConfigsResult describeConfigs = admin.describeConfigs(Collections.singleton(cr));
                describeConfigs.all().get().forEach((broker, config) -> config.entries().forEach(configEntry -> logger.info(String.format("config entry: (%s= %s)", configEntry.name(), configEntry.value()))));
            }

            logger.info("== Get topic information.");
            Map<String, TopicDescription> topicInformation = admin.describeTopics(Collections.singleton("test")).all().get();
            logger.info("topic information: {}", topicInformation);
        }
    }
}
