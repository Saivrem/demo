package com.example.demo;

import com.example.demo.persistence.EmployeeRepository;
import com.example.demo.persistence.TaskRepository;
import com.example.demo.persistence.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = {DemoApplication.class}, loader = SpringBootContextLoader.class)
class DemoApplicationTests {

    private final static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("postgres");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "postgres");
    }

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TaskRepository taskRepository;

    @Test
    @Sql("/data/insert.sql")
    void contextLoads() {
        List<Employee> all = employeeRepository.findAll();
        Assertions.assertFalse(all.isEmpty());
        Assertions.fail("Meant to fail here");
    }
}
