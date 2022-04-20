package org.telematix.api.handlers;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.telematix.services.ServiceException;
import org.telematix.validators.ValidationException;

@ControllerAdvice
public class ServiceExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<Object> handleServiceError(ServiceException e) {
        logger.error(e.getMessage(), e);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}
