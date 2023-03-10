package com.example.demo.persistence;

import com.example.demo.persistence.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUniqueNumber(String uniqueNumber);

    void deleteByUniqueNumber(String uniqueNumber);

}
