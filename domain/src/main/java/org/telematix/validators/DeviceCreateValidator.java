package org.telematix.validators;

import org.telematix.dto.device.DeviceCreateDto;

public class DeviceCreateValidator extends DtoValidator<DeviceCreateDto> {
    private static final int NAME_MIN_LENGTH = 2;

    @DtoField(name = "name", type = String.class)
    private final Validator<String> nameValidator = new StringValidator(NAME_MIN_LENGTH);

    @DtoField(name = "gps", type = Object.class)
    private final Validator<Boolean> gpsValidator = new NotNullValidator<>();
}
