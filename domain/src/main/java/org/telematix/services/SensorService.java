package org.telematix.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telematix.dto.device.DeviceResponseDto;
import org.telematix.dto.message.TopicMessageDto;
import org.telematix.dto.sensor.SensorCreateDto;
import org.telematix.dto.sensor.SensorResponseDto;
import org.telematix.dto.sensor.SensorUpdateDto;
import org.telematix.models.TopicMessage;
import org.telematix.models.sensor.Sensor;
import org.telematix.repositories.MessageRepository;
import org.telematix.repositories.SensorRepository;
import org.telematix.validators.SensorCreateValidator;
import org.telematix.validators.Validator;

@Service
public class SensorService {
    private static final String SENSOR_WAS_NOT_CREATED = "Sensor was not created.";
    private static final String SENSOR_NOT_FOUND = "Sensor not found.";
    private static final String SENSOR_IS_NOT_PART_OF_DEVICE = "Sensor is not part of device.";
    private static final String MESSAGE_NOT_FOUND = "Message not found";
    private final SensorRepository sensorRepository;
    private final MessageRepository messageRepository;
    private final DeviceService deviceService;
    private final Validator<SensorCreateDto> sensorCreateValidator = new SensorCreateValidator();

    public SensorService(SensorRepository sensorRepository, MessageRepository messageRepository, DeviceService deviceService) {
        this.sensorRepository = sensorRepository;
        this.messageRepository = messageRepository;
        this.deviceService = deviceService;
    }

    public List<SensorResponseDto> listSensors(int deviceId) {
        deviceService.getUserDevice(deviceId);
        return sensorRepository.getSensorsByDevice(deviceId).stream().map(SensorResponseDto::new).toList();

    }

    public SensorResponseDto createSensor(int deviceId, SensorCreateDto sensorCreateDto) {
        DeviceResponseDto device = deviceService.getUserDevice(deviceId);
        sensorCreateValidator.validate(sensorCreateDto);
        Sensor sensor = sensorCreateDto.toModel();
        sensor.setDeviceId(device.getId());
        Optional<Sensor> optionalSensor = sensorRepository.saveItem(sensor);
        if (optionalSensor.isEmpty()) throw new ItemNotFoundException(SENSOR_WAS_NOT_CREATED);
        return new SensorResponseDto(optionalSensor.get());
    }

    public SensorResponseDto getSensorById(int deviceId, int sensorId) {
        deviceService.getUserDevice(deviceId);
        Optional<Sensor> sensorOptional = sensorRepository.getById(sensorId);
        if (sensorOptional.isEmpty()) throw new ItemNotFoundException(SENSOR_NOT_FOUND);
        Sensor sensor = sensorOptional.get();
        if (sensor.getDeviceId() != deviceId) throw new ServiceException(SENSOR_IS_NOT_PART_OF_DEVICE);
        return new SensorResponseDto(sensor);
    }

    public SensorResponseDto updateSensor(int deviceId, int sensorId, SensorUpdateDto sensorUpdateDto) {
        deviceService.getUserDevice(deviceId);
        Optional<Sensor> sensorOptional = sensorRepository.updateItem(sensorId, sensorUpdateDto.toModel());
        if (sensorOptional.isEmpty()) throw new ItemNotFoundException(SENSOR_NOT_FOUND);
        Sensor sensor = sensorOptional.get();
        if (sensor.getDeviceId() != deviceId) throw new ServiceException(SENSOR_IS_NOT_PART_OF_DEVICE);
        return new SensorResponseDto(sensor);
    }

    public void deleteSensor(int deviceId, int sensorId) {
        deviceService.getUserDevice(deviceId);
        Optional<Sensor> sensorOptional = sensorRepository.getById(sensorId);
        if (sensorOptional.isEmpty()) return;
        if (sensorOptional.get().getDeviceId() != deviceId) throw new ServiceException(SENSOR_IS_NOT_PART_OF_DEVICE);
        sensorRepository.deleteItem(sensorId);
    }

    public TopicMessageDto getLatestMessage(int deviceId, int sensorId) {
        SensorResponseDto sensor = getSensorById(deviceId, sensorId);
        Optional<TopicMessage> topicMessageOptional = messageRepository.getLatestSensorMessage(sensor.getId());
        if (topicMessageOptional.isEmpty()) throw new ItemNotFoundException(MESSAGE_NOT_FOUND);
        return new TopicMessageDto(topicMessageOptional.get());
    }

    public List<TopicMessageDto> getMessagesByInterval(int deviceId, int sensorId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        SensorResponseDto sensor = getSensorById(deviceId, sensorId);
        return messageRepository
                .getSensorMessagesByInterval(sensor.getId(), dateFrom, dateTo)
                .stream()
                .map(TopicMessageDto::new).toList();
    }
}
