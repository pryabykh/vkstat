package com.pryabykh.vkstat.http.controllers;

import com.pryabykh.vkstat.core.dto.RegisteredUserDTO;
import com.pryabykh.vkstat.core.dto.RegistrationDataDTO;
import com.pryabykh.vkstat.core.services.UserService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.exceptions.VkAuthErrorException;
import org.mockserver.model.HttpStatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody RegistrationDataDTO registrationDataDTO) {
        try {
            RegisteredUserDTO registeredUserDTO = userService.register(registrationDataDTO);
            return ResponseEntity.ok(registeredUserDTO);
        } catch (NoSuchFieldValidationException | InvalidDtoException | ValidationErrorException e) {
            return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
        } catch (VkAuthErrorException e) {
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
