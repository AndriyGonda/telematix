package org.telematix.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telematix.dto.device.DeviceCreateDto;
import org.telematix.dto.device.DeviceResponseDto;
import org.telematix.dto.device.DeviceUpdateDto;
import org.telematix.models.Device;
import org.telematix.models.User;
import org.telematix.repositories.DeviceRepository;
import org.telematix.validators.DeviceCreateValidator;
import org.telematix.validators.Validator;

@Service
public class DeviceService {
    public static final String DEVICE_WAS_NOT_CREATED = "Device was not created.";
    public static final String DEVICE_NOT_FOUND = "Device not found.";
    private final DeviceRepository deviceRepository;
    private final AuthService authService;
    private final Validator<DeviceCreateDto> deviceCreateDtoValidator = new DeviceCreateValidator();

    public DeviceService(DeviceRepository deviceRepository, AuthService authService) {
        this.deviceRepository = deviceRepository;
        this.authService = authService;
    }

    public DeviceResponseDto createDevice(DeviceCreateDto deviceCreateDto) {
        User user = authService.getAuthUser();
        deviceCreateDtoValidator.validate(deviceCreateDto);
        Device device = deviceCreateDto.toModel();
        device.setUserId(user.getId());
        Optional<Device> createdDevice = deviceRepository.saveItem(device);
        if (createdDevice.isEmpty()) throw new ItemNotFoundException(DEVICE_WAS_NOT_CREATED);
        return new DeviceResponseDto(createdDevice.get());
    }

    public List<DeviceResponseDto> listUserDevices() {
        return deviceRepository.filterDevicesByUserId(authService.getAuthUser().getId()).stream().map(DeviceResponseDto::new).toList();
    }

    public DeviceResponseDto getUserDevice(int deviceId) {
        Optional<Device> optionalDevice = deviceRepository.getByUserAndId(authService.getAuthUser().getId(), deviceId);
        if (optionalDevice.isEmpty()) throw new ItemNotFoundException(DEVICE_NOT_FOUND);
        return new DeviceResponseDto(optionalDevice.get());
    }

    public DeviceResponseDto updateDevice(int deviceId, DeviceUpdateDto deviceUpdateDto) {
        Optional<Device> optionalDevice = deviceRepository.getByUserAndId(authService.getAuthUser().getId(), deviceId);
        if (optionalDevice.isEmpty()) throw new ItemNotFoundException(DEVICE_NOT_FOUND);
        Device updateDevice = deviceUpdateDto.toModel();
        updateDevice.setUserId(optionalDevice.get().getUserId());
        Optional<Device> updatedDevice = deviceRepository.updateItem(deviceId, updateDevice);
        if (updatedDevice.isEmpty()) throw new ItemNotFoundException(DEVICE_NOT_FOUND);
        return new DeviceResponseDto(updatedDevice.get());
    }

    public void deleteDevice(int deviceId) {
        deviceRepository.getByUserAndId(authService.getAuthUser().getId(), deviceId).ifPresent(device -> {
            deviceRepository.deleteItem(device.getId());
        });
    }
}
