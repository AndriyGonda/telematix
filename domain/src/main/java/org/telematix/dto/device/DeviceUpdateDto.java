package org.telematix.dto.device;

import org.telematix.models.Device;

public class DeviceUpdateDto {
    private String name;
    private boolean gps;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isGps() {
        return gps;
    }

    public Device toModel() {
        Device device = new Device();
        device.setName(name);
        device.setGps(gps);
        return device;
    }
}
