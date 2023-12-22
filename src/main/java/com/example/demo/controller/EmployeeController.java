package com.example.demo.controller;

import com.example.demo.persistence.entity.Employee;
import com.example.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${application.endpoint.employee}")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees() {
        return ResponseEntity.ok().body(employeeService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateEmployee(@RequestBody final Employee employee) {
        employeeService.create(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByUniqueNumber(@RequestBody final List<Employee> employee) {
        employeeService.delete(employee);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(employee);
    }
}
