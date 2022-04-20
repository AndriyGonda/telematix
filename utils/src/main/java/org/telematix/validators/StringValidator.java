package org.telematix.validators;

public class StringValidator implements Validator<String> {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 255;
    private static final String EMPTY_STRING_DOES_NOT_PERFORMED = "Empty string does not performed.";
    private static final String SHORTER_THAN_REQUIRED_LENGTH_FORMATTER = "The string '%s' shorter than required. Length %d is required.";
    private final int minLength;
    private final int maxLength;
    private final boolean allowEmpty;

    public StringValidator(int minLength) {
        this(minLength, MAX_LENGTH, false);
    }

    public StringValidator() {
        this(MIN_LENGTH, MAX_LENGTH, false);
    }

    public StringValidator(int minLength, int maxLength, boolean allowEmpty) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.allowEmpty = allowEmpty;
    }

    private void validateEmpty(String string) throws ValidationException {
        if (allowEmpty) return;
        if (string.isEmpty()) throw new ValidationException(EMPTY_STRING_DOES_NOT_PERFORMED);
    }

    private void validateMinLength(String string) throws ValidationException {
        if (string.length() < minLength) throw new ValidationException(
                String.format(SHORTER_THAN_REQUIRED_LENGTH_FORMATTER, string, minLength)
        );
    }

    private void validateMaxLength(String string) throws ValidationException {
        if (string.length() > maxLength)
            throw new ValidationException(
                    String.format(SHORTER_THAN_REQUIRED_LENGTH_FORMATTER, string, maxLength)
            );
    }

    @Override
    public void validate(String string) throws ValidationException {
        validateEmpty(string);
        validateMinLength(string);
        validateMaxLength(string);
    }
}