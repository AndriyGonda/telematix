package org.telematix.validators;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotNullValidatorTest {

    Validator<Integer> integerValidator;

    @BeforeEach
    void setUp() {
        integerValidator = new NotNullValidator<>();
    }

    @Test
    void validate_without_errors() {
        assertDoesNotThrow(() -> integerValidator.validate(1));
    }

    @Test
    void failure_to_validate_is_null() {
        assertThrows(ValidationException.class, () -> integerValidator.validate(null));
    }
}