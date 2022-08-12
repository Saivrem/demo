package com.example.demo.controller;

import com.example.demo.exception.ValidationException;
import com.example.demo.persistence.entity.Employee;
import com.example.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${application.endpoint.employee}")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees() {
        return ResponseEntity.ok().body(employeeService.getAllEmployees());
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateEmployee(@RequestBody final Employee employee) {
        try {
            employeeService.save(employee);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getViolations());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
