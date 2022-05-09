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
import org.springframework.web.bind.annotation.RestController;
import org.telematix.dto.sensor.SensorCreateDto;
import org.telematix.dto.sensor.SensorResponseDto;
import org.telematix.dto.sensor.SensorUpdateDto;
import org.telematix.services.SensorService;

@Tag(name = "Sensors")
@RestController("/api")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/device/{deviceId}/sensors")
    public List<SensorResponseDto> getDeviceSensors(@PathVariable("deviceId") int deviceId) {
        return sensorService.listSensors(deviceId);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/device/{deviceId}/sensors")
    public SensorResponseDto createSensor(@PathVariable("deviceId") int deviceId, @RequestBody SensorCreateDto sensorCreateDto) {
        return sensorService.createSensor(deviceId, sensorCreateDto);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/device/{deviceId}/sensor/{sensorId}")
    public SensorResponseDto getSensorById(@PathVariable("deviceId") int deviceId, @PathVariable("sensorId") int sensorId) {
        return sensorService.getSensorById(deviceId, sensorId);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/device/{deviceId}/sensor/{sensorId}")
    public SensorResponseDto updateSensor(
            @PathVariable("deviceId") int deviceId,
            @PathVariable("sensorId") int sensorId,
            @RequestBody SensorUpdateDto sensorUpdateDto) {
        return sensorService.updateSensor(deviceId, sensorId, sensorUpdateDto);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/device/{deviceId}/sensor/{sensorId}")
    public void deleteSensor(
            @PathVariable("deviceId") int deviceId,
            @PathVariable("sensorId") int sensorId
    ) {
        sensorService.deleteSensor(deviceId, sensorId);
    }
}