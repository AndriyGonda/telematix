package org.telematix.validators;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringValidatorTest {
    static final int MIN_LENGTH = 2;
    static final int MAX_LENGTH = 5;
    Validator<String> validator;

    @BeforeEach
    void setUp() {
        validator = new StringValidator(MIN_LENGTH, MAX_LENGTH, false);
    }

    @Test
    void validate_without_errors() {
        assertDoesNotThrow(() -> validator.validate("test"));
    }

    @Test
    void failure_to_validate_because_short() {
        assertThrows(ValidationException.class, () -> validator.validate("1"));
    }

    @Test
    void failure_to_validate_because_empty() {
        assertThrows(ValidationException.class, () -> validator.validate(""));
    }

    @Test
    void failure_to_validate_because_long() {
        assertThrows(ValidationException.class, () -> validator.validate("123456"));
    }
}