package org.telematix.validators;

public interface Validator<T> {
    void validate(T instance) throws ValidationException;
}
