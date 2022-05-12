package com.pryabykh.vkstat.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pryabykh.vkstat.core.dto.AddTaskDTO;
import com.pryabykh.vkstat.core.dto.CancelTaskDTO;
import com.pryabykh.vkstat.core.dto.RegisteredUserDTO;
import com.pryabykh.vkstat.core.dto.RegistrationDataDTO;
import com.pryabykh.vkstat.core.services.TaskService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import com.pryabykh.vkstat.core.vk.exceptions.VkAuthErrorException;
import com.pryabykh.vkstat.http.controllers.TaskController;
import com.pryabykh.vkstat.http.controllers.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TaskControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    public void addTaskPositive() throws Exception {
        Mockito.when(taskService.addTask(Mockito.any())).thenReturn(true);

        mockMvc.perform(post("/v1/tasks/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeAddTaskDataJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void addTaskInvalidDto() throws Exception {
        Mockito.when(taskService.addTask(Mockito.any())).thenThrow(InvalidDtoException.class);

        mockMvc.perform(post("/v1/tasks/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeAddTaskDataJson()))
                .andExpect(status().is(400));
    }

    @Test
    public void addTaskInternalServerError() throws Exception {
        Mockito.when(taskService.addTask(Mockito.any())).thenThrow(RequestToVkApiFailedException.class);

        mockMvc.perform(post("/v1/tasks/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeAddTaskDataJson()))
                .andExpect(status().is(500));
    }

    @Test
    public void cancelTaskPositive() throws Exception {
        Mockito.when(taskService.cancelTask(Mockito.any())).thenReturn(true);

        mockMvc.perform(post("/v1/tasks/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeCancelTaskDataJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void cancelTaskInvalidDto() throws Exception {
        Mockito.when(taskService.cancelTask(Mockito.any())).thenThrow(InvalidDtoException.class);

        mockMvc.perform(post("/v1/tasks/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeCancelTaskDataJson()))
                .andExpect(status().is(400));
    }

    @Test
    public void cancelTaskInternalServerError() throws Exception {
        Mockito.when(taskService.cancelTask(Mockito.any())).thenThrow(RequestToVkApiFailedException.class);

        mockMvc.perform(post("/v1/tasks/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeCancelTaskDataJson()))
                .andExpect(status().is(500));
    }

    private String shapeAddTaskDataJson() throws JsonProcessingException {
        AddTaskDTO addTaskDTO = new AddTaskDTO();
        addTaskDTO.setVkUserId("sergdevguy");
        addTaskDTO.setOwnerVkId(683257828);
        addTaskDTO.setPeriod(2000L);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(addTaskDTO);
    }

    private String shapeCancelTaskDataJson() throws JsonProcessingException {
        CancelTaskDTO cancelTaskDTO = new CancelTaskDTO();
        cancelTaskDTO.setVkUserId("sergdevguy");
        cancelTaskDTO.setOwnerVkId(683257828);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(cancelTaskDTO);
    }
}
