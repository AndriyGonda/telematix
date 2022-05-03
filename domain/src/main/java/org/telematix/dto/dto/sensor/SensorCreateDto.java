package org.telematix.dto.dto.sensor;

import org.telematix.models.sensor.Sensor;
import org.telematix.models.sensor.SensorType;

public class SensorCreateDto {
    private SensorType sensorType;
    private String topic;
    private String title;
    private int deviceId;

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }


}
