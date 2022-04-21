package org.telematix.validators;

import org.telematix.dto.user.UserCreateDto;

public class UserCreateValidator extends DtoValidator<UserCreateDto> {
    private static final  int MIN_USERNAME_LENGTH = 2;
    private static  final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_EMAIL_LENGTH = 3;

    @DtoField(name = "username", type = String.class)
    private final Validator<String> usernameValidator = new StringValidator(MIN_USERNAME_LENGTH);

    @DtoField(name = "email", type = String.class)
    private final Validator<String> emailValidator = new StringValidator(MIN_EMAIL_LENGTH);

    @DtoField(name = "password", type = String.class)
    private final Validator<String> passwordValidator = new StringValidator(MIN_PASSWORD_LENGTH);

}
