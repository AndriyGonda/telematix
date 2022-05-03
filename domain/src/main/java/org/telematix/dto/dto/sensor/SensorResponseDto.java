package org.telematix.dto.dto.sensor;

import org.telematix.models.sensor.Sensor;
import org.telematix.models.sensor.SensorType;

public class SensorResponseDto {

    private int id;
    private SensorType sensorType;
    private String topic;
    private String title;
    private int deviceId;

    public SensorResponseDto(Sensor sensor) {
        id = sensor.getId();
        sensorType = sensor.getSensorType();
        topic = sensor.getTopic();
        title = sensor.getTitle();
        deviceId = sensor.getDeviceId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
