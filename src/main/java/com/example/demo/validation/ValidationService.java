package com.example.demo.validation;

import com.example.demo.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Service
@RequiredArgsConstructor
public class ValidationService<T> {

    private final Validator validator;

    public boolean isValid(T t) throws ValidationException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);

        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            throw new ValidationException(buildViolationsList(constraintViolations));
        }
        return true;
    }

    private List<Violation> buildViolationsList(Set<ConstraintViolation<T>> constraintViolations) {
        return constraintViolations.stream()
                                   .map(violation -> new Violation(
                                                   violation.getPropertyPath().toString(),
                                                   violation.getMessage()
                                           )
                                   )
                                   .toList();
    }
}
