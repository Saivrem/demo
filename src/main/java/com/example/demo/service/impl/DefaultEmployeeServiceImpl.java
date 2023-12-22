package com.example.demo.service.impl;

import com.example.demo.persistence.EmployeeRepository;
import com.example.demo.persistence.entity.Employee;
import com.example.demo.service.EmployeeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class DefaultEmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional
    public void create(Employee employee) {
        Employee existingEmployee = employeeRepository.findByUniqueNumber(employee.getUniqueNumber());
        if (existingEmployee == null) {
            employeeRepository.save(employee);
        } else {
            existingEmployee = updateFields(existingEmployee, employee);
            employeeRepository.save(existingEmployee);
        }
    }

    @Override
    @Transactional
    public void delete(@NotEmpty List<@Valid Employee> employeeList) {
        employeeList.forEach(employee -> {
            Optional.ofNullable(employeeRepository.findByUniqueNumber(employee.getUniqueNumber()))
                    .map(Employee::getTasks).stream()
                    .flatMap(List::stream)
                    .filter(Objects::nonNull)
                    .forEach(task -> task.setEmployee(null));
            employeeRepository.deleteByUniqueNumber(employee.getUniqueNumber());
        });
    }

    private Employee updateFields(Employee existingEmployee, Employee updatedEmployee) {
        return existingEmployee.setDepartment(updatedEmployee.getDepartment())
                .setSalary(updatedEmployee.getSalary());
    }
}
