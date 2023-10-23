package com.example.demo.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

@Entity
@Data
@Table(indexes = @Index(name = "unique_number_idx", columnList = "uniqueNumber"))
@Accessors(chain = true)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;

    @NotBlank
    @Column(unique = true)
    private String uniqueNumber;
    private String firstName;
    private String lastName;
    private String department;
    private Double salary;
    private LocalDate hired;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    List<Task> tasks = new ArrayList<>();
}
