package org.telematix.dto.message;

import java.sql.Timestamp;
import org.telematix.models.TopicMessage;

public class TopicMessageDto {
    private String raw;
    private int sensorId;
    private Timestamp timestamp;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public TopicMessageDto(TopicMessage topicMessage) {
        this.raw = topicMessage.getRaw();
        this.sensorId = topicMessage.getSensorId();
        this.timestamp = topicMessage.getTimestamp();
    }
}
