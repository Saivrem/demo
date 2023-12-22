package com.example.demo.service;

import com.example.demo.exception.ProblemReport;
import com.example.demo.exception.ValidationException;
import com.example.demo.persistence.EmployeeRepository;
import com.example.demo.persistence.TaskRepository;
import com.example.demo.persistence.entity.Employee;
import com.example.demo.persistence.entity.Task;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

import static com.example.demo.utils.FieldUtils.getValueFromJsonNode;

@Service
@Validated
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    public List<Task> findUnassignedTasks() {
        return taskRepository.findAllByEmployeeIsNull();
    }

    @Transactional
    public String createOrUpdateTask(@Valid Task task) throws ValidationException {
        Task existingTask = taskRepository.findByTaskKey(task.getTaskKey());
        if (existingTask == null) {
            task.setCreatedDate(LocalDate.now());
            taskRepository.save(task);
        } else {
            taskRepository.save(updateFields(existingTask, task));
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
                        throw new ValidationException(List.of(new ProblemReport("Error", "task is already assigned to user")));
                    }
                } else {
                    byTaskKey.setEmployee(byUniqueNumber);
                }
                taskRepository.save(byTaskKey);
            } else {
                throw new ValidationException(
                        List.of(new ProblemReport("Not such task or employee found",
                                String.format("Task: %s, Employee: %s", byTaskKey, byUniqueNumber))));
            }
        } else {
            throw new ValidationException(List.of(new ProblemReport("Error", "taskKey or employee uniqueNumber should not be blank")));
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

