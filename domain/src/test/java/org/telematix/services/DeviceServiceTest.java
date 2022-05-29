package org.telematix.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telematix.dto.device.DeviceCreateDto;
import org.telematix.dto.device.DeviceResponseDto;
import org.telematix.dto.device.DeviceUpdateDto;
import org.telematix.models.Device;
import org.telematix.models.User;
import org.telematix.repositories.DeviceRepository;
import org.telematix.validators.ValidationException;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    AuthService authService;

    @Mock
    DeviceRepository deviceRepository;

    DeviceService deviceService;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPasswordHash("test");
        doReturn(user).when(authService).getAuthUser();
        deviceService = new DeviceService(deviceRepository, authService);
    }

    @Test
    void get_user_device_without_errors() {
        Device device = new Device();
        device.setUserId(1);
        device.setId(1);
        device.setName("test");
        doReturn(Optional.of(device)).when(deviceRepository).getByUserAndId(1, 1);
        DeviceResponseDto expectedDeviceDto = new DeviceResponseDto(device);
        DeviceResponseDto actualDeviceDto = deviceService.getUserDevice(1);
        assertEquals(expectedDeviceDto, actualDeviceDto);
    }

    @Test
    void get_user_device_failed_not_found() {
        doReturn(Optional.empty()).when(deviceRepository).getByUserAndId(1, 1);
        assertThrows(ItemNotFoundException.class, () -> deviceService.getUserDevice(1));
    }

    @Test
    void create_device_without_errors() {
        DeviceCreateDto deviceCreateDto = new DeviceCreateDto();
        deviceCreateDto.setName("test");
        Device device = deviceCreateDto.toModel();
        device.setUserId(1);
        doReturn(Optional.of(device)).when(deviceRepository).saveItem(device);
        DeviceResponseDto expectedDeviceDto = new DeviceResponseDto(device);
        DeviceResponseDto actualDeviceDto = deviceService.createDevice(deviceCreateDto);
        assertEquals(expectedDeviceDto, actualDeviceDto);
    }

    @Test
    void create_device_failed_name_short() {
        DeviceCreateDto deviceCreateDto = new DeviceCreateDto();
        deviceCreateDto.setName("t");
        assertThrows(ValidationException.class, () -> deviceService.createDevice(deviceCreateDto));
    }

    @Test
    void create_device_failed_result_empty() {
        DeviceCreateDto deviceCreateDto = new DeviceCreateDto();
        deviceCreateDto.setName("test");
        Device device = deviceCreateDto.toModel();
        device.setUserId(1);
        doReturn(Optional.empty()).when(deviceRepository).saveItem(device);
        assertThrows(ItemNotFoundException.class, () -> deviceService.createDevice(deviceCreateDto));
    }

    @Test
    void update_device_without_errors() {
        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto();
        deviceUpdateDto.setName("test");
        Device device = deviceUpdateDto.toModel();
        doReturn(Optional.of(device)).when(deviceRepository).getByUserAndId(1, 1);
        doReturn(Optional.of(device)).when(deviceRepository).updateItem(1, device);
        DeviceResponseDto expectedDeviceDto = new DeviceResponseDto(device);
        DeviceResponseDto actualDeviceDto = deviceService.updateDevice(1, deviceUpdateDto);
        assertEquals(expectedDeviceDto, actualDeviceDto);
    }

    @Test
    void update_device_failed_device_not_found_for_user() {
        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto();
        deviceUpdateDto.setName("test");
        doReturn(Optional.empty()).when(deviceRepository).getByUserAndId(1, 1);
        assertThrows(ItemNotFoundException.class, () -> deviceService.updateDevice(1, deviceUpdateDto));
    }

    @Test
    void update_device_failed_result_empty() {
        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto();
        deviceUpdateDto.setName("test");
        Device device = deviceUpdateDto.toModel();
        doReturn(Optional.of(device)).when(deviceRepository).getByUserAndId(1, 1);
        doReturn(Optional.empty()).when(deviceRepository).updateItem(1, device);
        assertThrows(ItemNotFoundException.class, () -> deviceService.updateDevice(1, deviceUpdateDto));
    }

    @Test
    void list_devices_without_errors() {
        doReturn(List.of(new Device())).when(deviceRepository).filterDevicesByUserId(1);
        assertDoesNotThrow(() -> deviceService.listUserDevices());
    }

    @Test
    void delete_device_without_errors() {
        Device device = new Device();
        device.setUserId(1);
        device.setId(1);
        device.setName("test");
        doReturn(Optional.of(device)).when(deviceRepository).getByUserAndId(1, 1);
        assertDoesNotThrow(() -> deviceService.deleteDevice(1));
    }
}