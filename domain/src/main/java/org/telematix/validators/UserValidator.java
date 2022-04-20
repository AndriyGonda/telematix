package org.telematix.validators;

import org.telematix.dto.user.UserCreateDto;

public class UserValidator extends DtoValidator<UserCreateDto> {
    private final static int MIN_USERNAME_LENGTH = 2;
    private final static int MIN_PASSWORD_LENGTH = 6;
    private final static int MIN_EMAIL_LENGTH = 3;

    @DtoField(name="username", type = String.class)
    private final Validator<String> usernameValidator = new StringValidator(MIN_USERNAME_LENGTH);

    @DtoField(name="email", type = String.class)
    private final Validator<String> emailValidator = new StringValidator(MIN_EMAIL_LENGTH);

    @DtoField(name="password", type = String.class)
    private final Validator<String> passwordValidator = new StringValidator(MIN_PASSWORD_LENGTH);

}
