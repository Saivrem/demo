package com.example.demo.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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

    @OneToMany(mappedBy = "employee")
    List<Task> tasks = new ArrayList<>();
}
