package org.telematix.validators;

import java.util.Objects;

public class NotNullValidator<T> implements Validator<T> {

    private static final String EMPTY_INSTANCE_DOES_NOT_PERFORMED = "Empty instance does not performed.";

    @Override
    public void validate(T instance) throws ValidationException {
        if (Objects.isNull(instance)) throw new ValidationException(EMPTY_INSTANCE_DOES_NOT_PERFORMED);
    }
}

