package com.example.demo.service;

import com.example.demo.exception.ValidationException;
import com.example.demo.persistence.EmployeeRepository;
import com.example.demo.persistence.TaskRepository;
import com.example.demo.persistence.entity.Employee;
import com.example.demo.persistence.entity.Task;
import com.example.demo.validation.ValidationService;
import com.example.demo.validation.Violation;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.utils.FieldUtils.getValueFromJsonNode;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final ValidationService<Task> validationService;

    public List<Task> findUnassignedTasks() {
        return taskRepository.findAllByEmployeeIsNull();
    }

    public String createOrUpdateTask(Task task) throws ValidationException {
        if (validationService.isValid(task)) {
            Task existingTask = taskRepository.findByTaskKey(task.getTaskKey());
            if (existingTask == null) {
                task.setCreatedDate(LocalDate.now());
                taskRepository.save(task);
            } else {
                taskRepository.save(updateFields(existingTask, task));
            }
        }
        return task.getTaskKey();
    }

    public void manageTask(JsonNode jsonNode) throws ValidationException {
        String taskKey = getValueFromJsonNode(jsonNode, "taskKey");
        String uniqueNumber = getValueFromJsonNode(jsonNode, "uniqueNumber");
        if (taskKey != null && uniqueNumber != null) {
            Employee byUniqueNumber = employeeRepository.findByUniqueNumber(uniqueNumber);
            Task byTaskKey = taskRepository.findByTaskKey(taskKey);
            if (byUniqueNumber != null && byTaskKey != null) {
                if (byTaskKey.getEmployee() != null) {
                    if (byTaskKey.getEmployee().getUniqueNumber().equals(uniqueNumber)) {
                        byTaskKey.setEmployee(null);
                    } else {
                        throw new ValidationException(List.of(new Violation("Error", "task is already assigned to user")));
                    }
                } else {
                    byTaskKey.setEmployee(byUniqueNumber);
                }
                taskRepository.save(byTaskKey);
            } else {
                throw new ValidationException(
                        List.of(new Violation("Not such task or employee found",
                                String.format("Task: %s, Employee: %s", byTaskKey, byUniqueNumber))));
            }
        } else {
            throw new ValidationException(List.of(new Violation("Error", "taskKey or employee uniqueNumber should not be blank")));
        }
    }

    public void deleteByTaskKey(JsonNode jsonNode) {
        String taskKey = getValueFromJsonNode(jsonNode, "taskKey");
        taskRepository.deleteTaskByTaskKey(taskKey);
    }


    private Task updateFields(Task existing, Task upcoming) {
        return existing.setTaskName(upcoming.getTaskName())
                .setDescription(upcoming.getDescription())
                .setDueDate(upcoming.getDueDate());
    }
}
