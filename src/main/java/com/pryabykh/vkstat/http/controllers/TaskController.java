package com.pryabykh.vkstat.http.controllers;

import com.pryabykh.vkstat.core.dto.AddTaskDTO;
import com.pryabykh.vkstat.core.dto.CancelTaskDTO;
import com.pryabykh.vkstat.core.services.TaskService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    ResponseEntity<?> add(@RequestBody AddTaskDTO addTaskDTO) {
        try {
            return ResponseEntity.ok(taskService.addTask(addTaskDTO));
        } catch (NoSuchFieldValidationException | ValidationErrorException | InvalidDtoException e) {
            return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel")
    ResponseEntity<?> cancel(@RequestBody CancelTaskDTO cancelTaskDTO) {
        try {
            taskService.cancelTask(cancelTaskDTO);
            return ResponseEntity.ok("Task canceled successfully");
        } catch (NoSuchFieldValidationException | ValidationErrorException | InvalidDtoException e) {
            return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
