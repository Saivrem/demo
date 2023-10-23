package com.example.demo.service;

import com.example.demo.exception.ValidationException;
import com.example.demo.persistence.EmployeeRepository;
import com.example.demo.persistence.entity.Employee;
import com.example.demo.validation.ValidationService;
import com.example.demo.validation.Violation;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.transaction.Transactional;

import static com.example.demo.utils.FieldUtils.getValueFromJsonNode;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ValidationService<Employee> validationService;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public void save(Employee employee) throws ValidationException {
        if (validationService.isValid(employee)) {
            Employee existingEmployee = employeeRepository.findByUniqueNumber(employee.getUniqueNumber());
            if (existingEmployee == null) {
                employeeRepository.save(employee);
            } else {
                existingEmployee = updateFields(existingEmployee, employee);
                employeeRepository.save(existingEmployee);
            }
        }
    }

    public void delete(JsonNode jsonNode) throws ValidationException {
        String uniqueNumber = getValueFromJsonNode(jsonNode, "uniqueNumber");
        if (uniqueNumber == null) {
            throw new ValidationException(List.of(new Violation("uniqueNumber", "must not be null")));
        }
        Optional.ofNullable(employeeRepository.findByUniqueNumber(uniqueNumber))
                .map(Employee::getTasks).stream()
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .forEach(task -> task.setEmployee(null));
        employeeRepository.deleteByUniqueNumber(uniqueNumber);
    }

    private Employee updateFields(Employee existingEmployee, Employee updatedEmployee) {
        return existingEmployee.setDepartment(updatedEmployee.getDepartment())
                               .setSalary(updatedEmployee.getSalary());
    }
}
