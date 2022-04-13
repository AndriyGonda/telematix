package org.telematix.models.sensor;

import java.util.Objects;

public class Sensor {
    private int id;
    private SensorType sensorType;
    private String topic;
    private String title;
    private int deviceId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return id == sensor.id && deviceId == sensor.deviceId && sensorType == sensor.sensorType && Objects.equals(topic, sensor.topic) && Objects.equals(title, sensor.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sensorType, topic, title, deviceId);
    }
}
