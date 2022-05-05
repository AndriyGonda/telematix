package org.telematix.api.user;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telematix.dto.device.DeviceCreateDto;
import org.telematix.dto.device.DeviceResponseDto;
import org.telematix.dto.device.DeviceUpdateDto;
import org.telematix.services.DeviceService;

@Tag(name = "Devices")
@RestController
@RequestMapping("/api")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/devices")
    public List<DeviceResponseDto> listDevices() {
        return deviceService.listUserDevices();
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/devices")
    public DeviceResponseDto createDevice(@RequestBody DeviceCreateDto deviceCreateDto) {
        return deviceService.createDevice(deviceCreateDto);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/device/{deviceId}")
    public DeviceResponseDto retrieveDevice(@PathVariable("deviceId") int deviceId) {
        return deviceService.getUserDevice(deviceId);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/device/{deviceId}")
    public DeviceResponseDto updateDevice(@PathVariable("deviceId") int deviceId, @RequestBody DeviceUpdateDto deviceUpdateDto) {
        return deviceService.updateDevice(deviceId, deviceUpdateDto);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/device/{deviceId}")
    public void deleteDevice(@PathVariable("deviceId") int deviceId) {
        deviceService.deleteDevice(deviceId);
    }
}
