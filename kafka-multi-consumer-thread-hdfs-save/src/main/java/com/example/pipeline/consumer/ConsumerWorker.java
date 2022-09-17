package com.example.pipeline.consumer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerWorker implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ConsumerWorker.class);
    private static final Map<Integer, List<String>> bufferString = new ConcurrentHashMap<>();
    private static final Map<Integer, Long> currentFileOffset = new ConcurrentHashMap<>();

    private static final int FLUSH_RECORD_COUNT = 10;
    private Properties props;
    private String topic;
    private String threadName;
    private KafkaConsumer<String, String> consumer;

    public ConsumerWorker(Properties props, String topic, int number) {
        log.info("Generate ConsumerWorker");
        this.props = props;
        this.topic = topic;
        this.threadName = "consumer-thread-" + number;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(this.threadName);
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> record : records) {
                    addHdfsFileBuffer(record);
                }
                saveBufferToHdfsFile(consumer.assignment());
            }
        } catch (WakeupException e) {
            log.warn("Wakeup consumer");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            consumer.close();
        }
    }

    public void stopAndWakeup() {
        log.info("stop and wakeup.");
        consumer.wakeup();
        saveRemainBufferToHdfsFile();
    }

    private void saveRemainBufferToHdfsFile() {
        bufferString.forEach((partitionNo, v) -> this.save(partitionNo));
    }

    private void saveBufferToHdfsFile(Set<TopicPartition> partitions) {
        partitions.forEach(p -> checkFlushCount((p.partition())));
    }

    private void checkFlushCount(int partitionNo) {
        List<String> records = bufferString.get(partitionNo);

        if (Objects.isNull(records)) {
            return;
        }

        if (records.size() >= FLUSH_RECORD_COUNT) {
            save(partitionNo);
        }
    }

    private void save(int partitionNo) {
        if (bufferString.get(partitionNo).isEmpty()) {
            return;
        }

        try {
            String fileName = "/data/color-" + partitionNo + "-" + currentFileOffset.get(partitionNo) + ".log";
            Configuration configuration = new Configuration();
            configuration.set("fs.defaultFS", "hdfs://localhost:9000");
            FileSystem hdfs = FileSystem.get(configuration);
            FSDataOutputStream fileOutputStream = hdfs.create(new Path(fileName));
            fileOutputStream.writeBytes(StringUtils.join("\n", bufferString.get(partitionNo)));
            fileOutputStream.close();

            bufferString.put(partitionNo, new ArrayList<>());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void addHdfsFileBuffer(ConsumerRecord<String, String> record) {
        List<String> buffer = bufferString.getOrDefault(record.partition(), new ArrayList<>());
        buffer.add(record.value());
        bufferString.put(record.partition(), buffer);

        if (buffer.size() == 1) {
            currentFileOffset.put(record.partition(), record.offset());
        }
    }
}