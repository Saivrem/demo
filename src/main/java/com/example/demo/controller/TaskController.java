package com.example.demo.controller;

import com.example.demo.exception.ValidationException;
import com.example.demo.persistence.entity.Task;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${application.endpoint.task}")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getUnassignedTasks() {
        return ResponseEntity.ok().body(taskService.findUnassignedTasks());
    }

    @PostMapping()
    public ResponseEntity<?> createOrUpdateTask(@RequestBody Task task) {
        try {
            String orUpdateTask = taskService.createOrUpdateTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(orUpdateTask);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getViolations());
        }
    }

    @PatchMapping()
    public ResponseEntity<?> assignEmployee(@RequestBody JsonNode jsonNode) {
        try {
            taskService.manageTask(jsonNode);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getViolations());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByUuid(@RequestBody JsonNode jsonNode) {
        taskService.deleteByTaskKey(jsonNode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
