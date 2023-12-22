package com.example.demo.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    ErrorMessage constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.warn("ConstraintViolationException: ", e);
        return new ErrorMessage(BAD_REQUEST.getReasonPhrase(), buildProblemReports(e.getConstraintViolations()));
    }

    private List<ProblemReport> buildProblemReports(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(cv -> new ProblemReport(cv.getPropertyPath().toString(), cv.getMessage()))
                .toList();
    }
}
