package com.example.demo.service;

import com.example.demo.persistence.EmployeeRepository;
import com.example.demo.persistence.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return List.of(new Employee().setId(123L));
    }
}
