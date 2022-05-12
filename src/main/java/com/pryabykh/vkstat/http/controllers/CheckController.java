package com.pryabykh.vkstat.http.controllers;

import com.pryabykh.vkstat.core.dto.AddTaskDTO;
import com.pryabykh.vkstat.core.dto.FetchAllRequestDTO;
import com.pryabykh.vkstat.core.services.CheckService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/checks")
public class CheckController {
    private final CheckService checkService;

    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @PostMapping
    ResponseEntity<?> fetchAll(@RequestBody FetchAllRequestDTO fetchAllRequestDTO) {
        try {
            return ResponseEntity.ok(checkService.fetchAll(fetchAllRequestDTO));
        } catch (NoSuchFieldValidationException | ValidationErrorException | InvalidDtoException e) {
            return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
