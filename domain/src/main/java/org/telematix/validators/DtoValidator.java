package org.telematix.validators;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class DtoValidator<T> implements Validator<T> {

    private static final String VALIDATION_FAILED = "Validation failed for field \"%s\" with error \"%s\"";

    @Override
    public void validate(T request) throws ValidationException {
        List<Field> validatorFields = Arrays
                .stream(this.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DtoField.class)).toList();

        validatorFields.forEach(field -> {
            DtoField annotation = field.getAnnotation(DtoField.class);
            try {
                field.setAccessible(true);
                Object validator = field.get(this);
                Method validate = validator.getClass().getMethod("validate", annotation.type());
                Field requestField = request.getClass().getDeclaredField(annotation.name());
                requestField.setAccessible(true);
                validate.invoke(validator, requestField.get(request));
            } catch (IllegalAccessException |
                    NoSuchMethodException |
                    NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Class<?> exceptionClass = e.getCause().getClass();
                if (exceptionClass == ValidationException.class) {
                    throw new ValidationException(
                            String.format(
                                    VALIDATION_FAILED,
                                    annotation.name(),
                                    e.getCause().getMessage()
                            )
                    );
                }
            }
        });
    }
}
