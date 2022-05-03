package org.telematix.dto.device;

import org.telematix.models.Device;

public class DeviceResponseDto {
    private int id;
    private String name;
    private int userId;
    private boolean gps;

    public DeviceResponseDto(Device device) {
        id = device.getId();
        userId = device.getUserId();
        name = device.getName();
        gps = device.isGps();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isGps() {
        return gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }
}
