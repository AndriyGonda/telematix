package org.telematix.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telematix.dto.device.DeviceResponseDto;
import org.telematix.dto.message.TopicMessageDto;
import org.telematix.dto.sensor.SensorCreateDto;
import org.telematix.dto.sensor.SensorResponseDto;
import org.telematix.dto.sensor.SensorUpdateDto;
import org.telematix.models.Device;
import org.telematix.models.TopicMessage;
import org.telematix.models.sensor.Sensor;
import org.telematix.models.sensor.SensorType;
import org.telematix.repositories.MessageRepository;
import org.telematix.repositories.RepositoryException;
import org.telematix.repositories.SensorRepository;

@ExtendWith(MockitoExtension.class)
class SensorServiceTest {

    @Mock
    SensorRepository sensorRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    DeviceService deviceService;

    SensorService sensorService;
    Device device;

    @BeforeEach
    void setUp() {
        sensorService = new SensorService(sensorRepository, messageRepository, deviceService);
        device = new Device();
        device.setUserId(1);
        device.setId(1);
    }


    @Test
    void get_sensor_by_id_without_errors() {
        Sensor sensor  = new Sensor();
        sensor.setSensorType(SensorType.STRING);
        sensor.setTopic("/test");
        sensor.setTitle("test");
        sensor.setDeviceId(1);
        sensor.setId(1);
        doReturn(Optional.of(sensor)).when(sensorRepository).getById(1);
        doReturn(new DeviceResponseDto(device)).when(deviceService).getUserDevice(1);
        SensorResponseDto expectedSensorDto = new SensorResponseDto(sensor);
        SensorResponseDto actualSensorDto = sensorService.getSensorById(1, 1);
        assertEquals(expectedSensorDto, actualSensorDto);
    }

    @Test
    void get_sensor_by_id_failed_device_not_found() {
        doThrow(new ItemNotFoundException("device not found")).when(deviceService).getUserDevice(1);
        assertThrows(ItemNotFoundException.class, () -> sensorService.getSensorById(1, 1));
    }

    @Test
    void get_sensor_by_id_failed_sensor_not_found() {
        doReturn(Optional.empty()).when(sensorRepository).getById(1);
        doReturn(new DeviceResponseDto(device)).when(deviceService).getUserDevice(1);
        assertThrows(ItemNotFoundException.class, () -> sensorService.getSensorById(1, 1));
    }

    @Test
    void get_latest_message_without_errors() {
        Sensor sensor  = new Sensor();
        sensor.setSensorType(SensorType.STRING);
        sensor.setTopic("/test");
        sensor.setTitle("test");
        sensor.setDeviceId(1);
        sensor.setId(1);
        doReturn(Optional.of(sensor)).when(sensorRepository).getById(1);
        doReturn(new DeviceResponseDto(device)).when(deviceService).getUserDevice(1);
        TopicMessage expectedMessage = new TopicMessage();
        expectedMessage.setRaw("test");
        expectedMessage.setId(1);
        doReturn(Optional.of(expectedMessage)).when(messageRepository).getLatestSensorMessage(1);
        TopicMessageDto expectedTopicMessage = new TopicMessageDto(expectedMessage);
        TopicMessageDto actualTopicMessage  = sensorService.getLatestMessage(1, 1);
        assertEquals(expectedTopicMessage, actualTopicMessage);
    }

    @Test
    void get_latest_message_failed_message_not_found() {
        Sensor sensor  = new Sensor();
        sensor.setSensorType(SensorType.STRING);
        sensor.setTopic("/test");
        sensor.setTitle("test");
        sensor.setDeviceId(1);
        sensor.setId(1);
        doReturn(Optional.of(sensor)).when(sensorRepository).getById(1);
        doReturn(new DeviceResponseDto(device)).when(deviceService).getUserDevice(1);
        doReturn(Optional.empty()).when(messageRepository).getLatestSensorMessage(1);
        assertThrows(ItemNotFoundException.class, () -> sensorService.getLatestMessage(1, 1));
    }

    @Test
    void create_sensor_without_errors() {
        SensorCreateDto sensorCreateDto = new SensorCreateDto();
        sensorCreateDto.setSensorType(SensorType.STRING);
        sensorCreateDto.setTitle("test");
        sensorCreateDto.setTopic("test");
        Sensor sensor = sensorCreateDto.toModel();
        sensor.setDeviceId(1);
        doReturn(Optional.of(sensor)).when(sensorRepository).saveItem(sensor);
        doReturn(new DeviceResponseDto(device)).when(deviceService).getUserDevice(1);
        SensorResponseDto expectedSensorDto = new SensorResponseDto(sensor);
        SensorResponseDto actualSensorDto = sensorService.createSensor(1, sensorCreateDto);
        assertEquals(expectedSensorDto, actualSensorDto);
    }

    @Test
    void create_sensor_failed_result_empty() {
        SensorCreateDto sensorCreateDto = new SensorCreateDto();
        sensorCreateDto.setSensorType(SensorType.STRING);
        sensorCreateDto.setTitle("test");
        sensorCreateDto.setTopic("test");
        Sensor sensor = sensorCreateDto.toModel();
        sensor.setDeviceId(1);
        doReturn(Optional.empty()).when(sensorRepository).saveItem(sensor);
        doReturn(new DeviceResponseDto(device)).when(deviceService).getUserDevice(1);
        assertThrows(ItemNotFoundException.class, () -> sensorService.createSensor(1, sensorCreateDto));
    }

    @Test
    void create_sensor_failed_duplicated_topic() {
        SensorCreateDto sensorCreateDto = new SensorCreateDto();
        sensorCreateDto.setSensorType(SensorType.STRING);
        sensorCreateDto.setTitle("test");
        sensorCreateDto.setTopic("test");
        Sensor sensor = sensorCreateDto.toModel();
        sensor.setDeviceId(1);
        doThrow(new RepositoryException("duplicated")).when(sensorRepository).saveItem(sensor);
        doReturn(new DeviceResponseDto(device)).when(deviceService).getUserDevice(1);
        assertThrows(ServiceException.class, () -> sensorService.createSensor(1, sensorCreateDto));
    }

}