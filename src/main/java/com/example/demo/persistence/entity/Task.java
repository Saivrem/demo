package com.example.demo.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Accessors(chain = true)
@Table(indexes = @Index(name = "task_key_idx", columnList = "taskKey"))
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long taskId;

    @Column(nullable = false)
    @NotBlank
    private String department;

    private String taskKey;

    private LocalDate createdDate;

    private LocalDate dueDate;

    @NotBlank
    private String taskName;

    @NotBlank
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    Employee employee;

    @PostPersist
    public void generateTaskKey() {
        if (taskKey == null) {
            taskKey = StringUtils.joinWith("-", department, taskId);
        }
    }
}
