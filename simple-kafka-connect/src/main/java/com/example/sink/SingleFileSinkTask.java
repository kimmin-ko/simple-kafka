package com.example.sink;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class SingleFileSinkTask extends SinkTask {

    private SingleFileSinkConnectorConfig config;
    private File file;
    private FileWriter fileWriter;

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> props) {
        try {
            this.config = new SingleFileSinkConnectorConfig(props);
            this.file = new File(this.config.getString(SingleFileSinkConnectorConfig.DIR_FILE_NAME));
            this.fileWriter = new FileWriter(file, true);
        } catch (Exception e) {
            throw new ConnectException(e.getMessage(), e);
        }
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        try {
            for (SinkRecord sinkRecord : records) {
                this.fileWriter.write(sinkRecord.value().toString() + "\n");
            }
        } catch (IOException e) {
            throw new ConnectException(e.getMessage(), e);
        }
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> currentOffsets) {
        try {
            this.fileWriter.flush();
        } catch (IOException e) {
            throw new ConnectException(e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        try {
            this.fileWriter.close();
        } catch (IOException e) {
            throw new ConnectException(e.getMessage(), e);
        }
    }
}
