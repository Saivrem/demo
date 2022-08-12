package com.example.demo.validation;

import com.example.demo.exception.ValidationException;
import com.example.demo.persistence.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final Validator validator;

    public boolean isValidEmployee(Employee employee) throws ValidationException {
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee);

        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            throw new ValidationException(buildViolationsList(constraintViolations));
        }
        return true;
    }

    private <T> List<Violation> buildViolationsList(Set<ConstraintViolation<T>> constraintViolations) {
        return constraintViolations.stream()
                                   .map(violation -> new Violation(
                                                   violation.getPropertyPath().toString(),
                                                   violation.getMessage()
                                           )
                                   )
                                   .toList();
    }
}
