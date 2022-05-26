package org.telematix.services;

import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telematix.dto.GeopositionDto;
import org.telematix.dto.device.DeviceResponseDto;
import org.telematix.dto.message.TopicMessageDto;
import org.telematix.dto.report.GpsResponseDto;
import org.telematix.dto.report.number.NumberResponseDto;
import org.telematix.dto.report.number.NumbersReportDto;
import org.telematix.dto.sensor.SensorCreateDto;
import org.telematix.dto.sensor.SensorResponseDto;
import org.telematix.dto.sensor.SensorUpdateDto;
import org.telematix.models.TopicMessage;
import org.telematix.models.sensor.Sensor;
import org.telematix.models.sensor.SensorType;
import org.telematix.repositories.MessageRepository;
import org.telematix.repositories.RepositoryException;
import org.telematix.repositories.SensorRepository;
import org.telematix.validators.SensorCreateValidator;
import org.telematix.validators.Validator;

@Service
public class SensorService {
    private static final String SENSOR_WAS_NOT_CREATED = "Sensor was not created.";
    private static final String SENSOR_NOT_FOUND = "Sensor not found.";
    private static final String SENSOR_IS_NOT_PART_OF_DEVICE = "Sensor is not part of device.";
    private static final String MESSAGE_NOT_FOUND = "Message not found";
    public static final String NON_GPS_SENSOR_TYPE = "Invalid sensor type for GPS coordinates report.";
    public static final String NON_NUMBER_SENSOR_TYPE = "The sensor is not number sensor.";
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
        try {
            DeviceResponseDto device = deviceService.getUserDevice(deviceId);
            sensorCreateValidator.validate(sensorCreateDto);
            Sensor sensor = sensorCreateDto.toModel();
            sensor.setDeviceId(device.getId());
            Optional<Sensor> optionalSensor = sensorRepository.saveItem(sensor);
            if (optionalSensor.isEmpty()) throw new ItemNotFoundException(SENSOR_WAS_NOT_CREATED);
            return new SensorResponseDto(optionalSensor.get());
        } catch (RepositoryException e) {
            throw  new ServiceException(e.getMessage());
        }

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
        try {
            deviceService.getUserDevice(deviceId);
            Optional<Sensor> sensorOptional = sensorRepository.updateItem(sensorId, sensorUpdateDto.toModel());
            if (sensorOptional.isEmpty()) throw new ItemNotFoundException(SENSOR_NOT_FOUND);
            Sensor sensor = sensorOptional.get();
            if (sensor.getDeviceId() != deviceId) throw new ServiceException(SENSOR_IS_NOT_PART_OF_DEVICE);
            return new SensorResponseDto(sensor);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }

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

    public List<GpsResponseDto> getGpsReport(int deviceId, int sensorId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        SensorResponseDto sensor = getSensorById(deviceId, sensorId);
        if (!sensor.getSensorType().equals(SensorType.GPS_JSON)) throw new ServiceException(NON_GPS_SENSOR_TYPE);
        List<TopicMessageDto> messages = getMessagesByInterval(deviceId, sensorId, dateFrom, dateTo);
        Gson gson = new Gson();
        return messages.stream().map(message -> {
            GeopositionDto location = gson.fromJson(message.getRaw(), GeopositionDto.class);
            GpsResponseDto gpsMessage = new GpsResponseDto();
            gpsMessage.setLatitude(location.getLatitude());
            gpsMessage.setLongitude(location.getLongitude());
            gpsMessage.setTimestamp(message.getTimestamp());
            return gpsMessage;
        }).toList();
    }

    public NumbersReportDto getNumbersReport(int deviceId, int sensorId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        SensorResponseDto sensor = getSensorById(deviceId, sensorId);
        if (!sensor.getSensorType().equals(SensorType.NUMBER)) throw new ServiceException(NON_NUMBER_SENSOR_TYPE);
        List<TopicMessageDto> messages = getMessagesByInterval(deviceId, sensorId, dateFrom, dateTo);
        List<NumberResponseDto> numberMessages = messages.stream().map(topicMessageDto -> {
            NumberResponseDto numberResponseDto = new NumberResponseDto();
            numberResponseDto.setValue(Float.parseFloat(topicMessageDto.getRaw()));
            numberResponseDto.setTimestamp(topicMessageDto.getTimestamp());
            return numberResponseDto;
        }).toList();
        return getNumbersReportDto(numberMessages);
    }

    private NumbersReportDto getNumbersReportDto(List<NumberResponseDto> numberMessages) {
        List<Float> values = numberMessages.stream().map(NumberResponseDto::getValue).toList();
        NumbersReportDto numbersReportDto = new NumbersReportDto();
        numbersReportDto.setMaxValue(values.stream().mapToDouble(value -> value).max().orElse(0));
        numbersReportDto.setMinValue(values.stream().mapToDouble(value -> value).min().orElse(0));
        numbersReportDto.setAverageValue(values.stream().mapToDouble(value -> value).average().orElse(0));
        numbersReportDto.setTotal(values.stream().mapToDouble(value -> value).sum());
        numbersReportDto.setMessages(numberMessages);
        return numbersReportDto;
    }
}
