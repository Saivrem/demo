package com.example.demo.persistence;

import com.example.demo.persistence.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByTaskKey(String taskUuid);

    List<Task> findAllByEmployeeIsNull();

    void deleteTaskByTaskKey(String taskKey);
}
