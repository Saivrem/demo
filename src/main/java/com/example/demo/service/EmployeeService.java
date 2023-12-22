package com.example.demo.service;

import com.example.demo.persistence.entity.Employee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();

    void create(@Valid Employee employee);

    void delete(@NotEmpty List<@Valid Employee> employee);
}
