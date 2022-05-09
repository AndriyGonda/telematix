package org.telematix.dto.sensor;

import org.telematix.models.sensor.Sensor;
import org.telematix.models.sensor.SensorType;

public class SensorUpdateDto {
    private SensorType sensorType;
    private String topic;
    private String title;

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public String getTopic() {
        return topic;
    }

    public String getTitle() {
        return title;
    }

    public Sensor toModel() {
        Sensor sensor = new Sensor();
        sensor.setTopic(topic);
        sensor.setTitle(title);
        sensor.setSensorType(sensorType);
        return sensor;
    }
}
