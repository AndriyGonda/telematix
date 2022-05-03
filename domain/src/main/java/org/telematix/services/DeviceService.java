package org.telematix.services;

import org.springframework.stereotype.Service;
import org.telematix.models.Device;
import org.telematix.repositories.ModelRepository;

@Service
public class DeviceService {
    private final ModelRepository<Device> deviceRepository;

    public DeviceService(ModelRepository<Device> deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

}
