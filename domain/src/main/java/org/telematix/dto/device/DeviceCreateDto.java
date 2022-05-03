package org.telematix.dto.device;

import org.telematix.models.Device;

public class DeviceCreateDto {
    private String name;
    private int userId;
    private boolean gps;

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isGps() {
        return gps;
    }

    public Device toModel() {
        Device device = new Device();
        device.setUserId(userId);
        device.setName(name);
        device.setGps(gps);
        return device;
    }
}
