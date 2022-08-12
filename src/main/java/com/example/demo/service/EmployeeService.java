package com.example.demo.service;

import com.example.demo.exception.ValidationException;
import com.example.demo.persistence.EmployeeRepository;
import com.example.demo.persistence.entity.Employee;
import com.example.demo.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ValidationService validationService;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public void save(Employee employee) throws ValidationException {
        if (validationService.isValidEmployee(employee)) {
            Employee existingEmployee = employeeRepository.findByUniqueNumber(employee.getUniqueNumber());
            if (existingEmployee == null) {
                employeeRepository.save(employee);
            } else {
                existingEmployee = updateFields(existingEmployee, employee);
                employeeRepository.save(existingEmployee);
            }
        }
    }

    private Employee updateFields(Employee existingEmployee, Employee updatedEmployee) {
        return existingEmployee.setDepartment(updatedEmployee.getDepartment())
                               .setSalary(updatedEmployee.getSalary())
                               .setTasks(updatedEmployee.getTasks());
    }
}
