package com.example;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SingleFileSourceTask extends SourceTask {

    private static final Logger logger = LoggerFactory.getLogger(SingleFileSourceTask.class);

    public static final String FILENAME_FIELD = "filename";
    public static final String POSITION_FIELD = "position";

    private Map<String, String> fileNamePartition;
    private Map<String, Object> offset;
    private String topic;
    private String file;
    private long position = -1;

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> props) {
        try {
            // Init variables
            SingleFileSourceConnectorConfig config = new SingleFileSourceConnectorConfig(props);
            this.topic = config.getString(SingleFileSourceConnectorConfig.TOPIC_NAME); // key로 value 조회 (TOPIC_NAME으로 TOPIC_VALUE 조회)
            this.file = config.getString(SingleFileSourceConnectorConfig.DIR_FILE_NAME); // key로 value 조회
            this.fileNamePartition = Collections.singletonMap(FILENAME_FIELD, this.file);
            this.offset = context.offsetStorageReader().offset(this.fileNamePartition);

            // Get file offset from offsetStorageReader
            if (this.offset != null) {
                Object lastReadFileOffset = this.offset.get(POSITION_FIELD);
                if (lastReadFileOffset != null) {
                    this.position = (Long) lastReadFileOffset;
                    return;
                }
            }

            this.position = 0;

        } catch (Exception e) {
            throw new ConnectException(e.getMessage(), e);
        }
    }

    @Override
    public List<SourceRecord> poll() {
        List<SourceRecord> results = new ArrayList<>();
        try {
            Thread.sleep(1000);

            List<String> lines = getLines(position);

            if (!lines.isEmpty()) {
                lines.forEach(line -> {
                    Map<String, Long> sourceOffset = Collections.singletonMap(POSITION_FIELD, ++position);
                    SourceRecord sourceRecord = new SourceRecord(fileNamePartition, sourceOffset, topic, Schema.STRING_SCHEMA, line);
                    results.add(sourceRecord);
                });
            }
            return results;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ConnectException(e.getMessage(), e);
        }
    }

    private List<String> getLines(long readLine) throws Exception {
        BufferedReader reader = Files.newBufferedReader(Paths.get(file));
        return reader.lines().skip(readLine).collect(Collectors.toList());
    }

    @Override
    public void stop() {

    }
}
