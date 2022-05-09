package org.telematix.validators;

import org.telematix.dto.sensor.SensorCreateDto;
import org.telematix.models.sensor.Sensor;
import org.telematix.models.sensor.SensorType;

public class SensorCreateValidator extends DtoValidator<SensorCreateDto> {
    @DtoField(name = "title", type = String.class)
    private final Validator<String> titleValidator = new StringValidator();

    @DtoField(name = "topic", type = String.class)
    private final Validator<String> topicValidator = new StringValidator();

    @DtoField(name = "sensorType", type = Object.class)
    private final Validator<SensorType> notNullSensorTypeValidator = new NotNullValidator<>();
}
