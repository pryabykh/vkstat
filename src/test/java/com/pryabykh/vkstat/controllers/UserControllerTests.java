package com.pryabykh.vkstat.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pryabykh.vkstat.core.dto.RegisteredUserDTO;
import com.pryabykh.vkstat.core.dto.RegistrationDataDTO;
import com.pryabykh.vkstat.core.services.UserService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.exceptions.VkAuthErrorException;
import com.pryabykh.vkstat.http.controllers.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void registerPositive() throws Exception {
        Mockito.when(userService.register(Mockito.any())).thenReturn(shapeRegisteredUser());

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeRegistrationDataJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.vkUserId", equalTo(683257828)))
                .andExpect(jsonPath("$.vkToken", equalTo("42086410a3104646e36e749ac45a3b3822d9dbc278e4223256365de18709c1485d21b8af9237cde48c157")));
    }

    @Test
    public void registerInvalidDto() throws Exception {
        Mockito.when(userService.register(Mockito.any())).thenThrow(InvalidDtoException.class);

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeRegistrationDataJson()))
                .andExpect(status().is(400));
    }

    @Test
    public void registerUnauthorized() throws Exception {
        Mockito.when(userService.register(Mockito.any())).thenThrow(VkAuthErrorException.class);

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeRegistrationDataJson()))
                .andExpect(status().is(401));
    }

    @Test
    public void registerInternalError() throws Exception {
        Mockito.when(userService.register(Mockito.any())).thenThrow(Exception.class);

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeRegistrationDataJson()))
                .andExpect(status().is(500));
    }

    private RegisteredUserDTO shapeRegisteredUser() {
        RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO();
        registeredUserDTO.setId(1L);
        registeredUserDTO.setVkUserId(683257828);
        registeredUserDTO.setVkToken("42086410a3104646e36e749ac45a3b3822d9dbc278e4223256365de18709c1485d21b8af9237cde48c157");
        return registeredUserDTO;
    }

    private String shapeRegistrationDataJson() throws JsonProcessingException {
        RegistrationDataDTO registrationDataDTO = new RegistrationDataDTO();
        registrationDataDTO.setCodeToGetToken("42086410a3104646e36e749ac45a3b3822d9dbc278e4223256365de18709c1485d21b8af9237cde48c157");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(registrationDataDTO);
    }
}
