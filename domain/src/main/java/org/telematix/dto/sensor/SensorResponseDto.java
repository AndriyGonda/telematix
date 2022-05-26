package org.telematix.dto.sensor;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorResponseDto that = (SensorResponseDto) o;
        return id == that.id && deviceId == that.deviceId && sensorType == that.sensorType && Objects.equals(topic, that.topic) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sensorType, topic, title, deviceId);
    }

    @Override
    public String toString() {
        return "SensorResponseDto{" +
                "id=" + id +
                ", sensorType=" + sensorType +
                ", topic='" + topic + '\'' +
                ", title='" + title + '\'' +
                ", deviceId=" + deviceId +
                '}';
    }
}
