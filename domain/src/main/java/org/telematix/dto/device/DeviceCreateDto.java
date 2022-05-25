package org.telematix.dto.device;

import org.telematix.models.Device;

public class DeviceCreateDto {
    private String name;
    private boolean gps;

    public void setName(String name) {
        this.name = name;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
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
