package com.pryabykh.vkstat.services;

import com.pryabykh.vkstat.core.db.Token;
import com.pryabykh.vkstat.core.db.User;
import com.pryabykh.vkstat.core.db.repositories.TokenRepository;
import com.pryabykh.vkstat.core.db.repositories.UserRepository;
import com.pryabykh.vkstat.core.dto.AddTaskDTO;
import com.pryabykh.vkstat.core.dto.CancelTaskDTO;
import com.pryabykh.vkstat.core.services.TaskManagerService;
import com.pryabykh.vkstat.core.services.TaskService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.VkService;
import com.pryabykh.vkstat.core.vk.ds.Online;
import com.pryabykh.vkstat.core.vk.ds.VkUser;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TaskServiceTests {
    private TaskService taskService;
    @MockBean
    private TaskManagerService taskManagerService;
    @MockBean
    private VkService vkService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TokenRepository tokenRepository;

    @Test
    public void addTaskPositive() throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        Mockito.when(vkService.getUser(Mockito.anyString(), Mockito.anyString())).thenReturn(shapeVkUser());
        Mockito.when(taskManagerService.run(Mockito.any())).thenReturn(true);
        Mockito.when(taskManagerService.cancel(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(taskManagerService.isRunning(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        Mockito.when(userRepository.findByVkId(Mockito.anyInt())).thenReturn(shapeUser());
        Mockito.when(tokenRepository.findByUserId(Mockito.anyLong())).thenReturn(shapeToken());

        boolean result = taskService.addTask(shapeAddTaskDto());
        Assertions.assertTrue(result);
    }

    @Test
    public void addTaskAlreadyRunning() throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        Mockito.when(vkService.getUser(Mockito.anyString(), Mockito.anyString())).thenReturn(shapeVkUser());
        Mockito.when(taskManagerService.run(Mockito.any())).thenReturn(true);
        Mockito.when(taskManagerService.cancel(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(taskManagerService.isRunning(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(userRepository.findByVkId(Mockito.anyInt())).thenReturn(shapeUser());
        Mockito.when(tokenRepository.findByUserId(Mockito.anyLong())).thenReturn(shapeToken());

        boolean result = taskService.addTask(shapeAddTaskDto());
        Assertions.assertFalse(result);
    }

    @Test
    public void addTaskInvalidDto() throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        Mockito.when(vkService.getUser(Mockito.anyString(), Mockito.anyString())).thenReturn(shapeVkUser());
        Mockito.when(taskManagerService.run(Mockito.any())).thenReturn(true);
        Mockito.when(taskManagerService.cancel(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(taskManagerService.isRunning(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        Mockito.when(userRepository.findByVkId(Mockito.anyInt())).thenReturn(shapeUser());
        Mockito.when(tokenRepository.findByUserId(Mockito.anyLong())).thenReturn(shapeToken());

        Assertions.assertThrows(InvalidDtoException.class, () -> {
            taskService.addTask(shapeInvalidAddTaskDto());
        });
    }

    @Test
    public void cancelTaskPositive() throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        Mockito.when(vkService.getUser(Mockito.anyString(), Mockito.anyString())).thenReturn(shapeVkUser());
        Mockito.when(taskManagerService.run(Mockito.any())).thenReturn(true);
        Mockito.when(taskManagerService.cancel(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(userRepository.findByVkId(Mockito.anyInt())).thenReturn(shapeUser());
        Mockito.when(tokenRepository.findByUserId(Mockito.anyLong())).thenReturn(shapeToken());

        boolean result = taskService.cancelTask(shapeCancelTaskDto());
        Assertions.assertTrue(result);
    }

    @Test
    public void cancelTaskInvalidDto() throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        Mockito.when(vkService.getUser(Mockito.anyString(), Mockito.anyString())).thenReturn(shapeVkUser());
        Mockito.when(taskManagerService.run(Mockito.any())).thenReturn(true);
        Mockito.when(taskManagerService.cancel(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(userRepository.findByVkId(Mockito.anyInt())).thenReturn(shapeUser());
        Mockito.when(tokenRepository.findByUserId(Mockito.anyLong())).thenReturn(shapeToken());

        Assertions.assertThrows(InvalidDtoException.class, () -> {
            taskService.cancelTask(shapeInvalidCancelTaskDto());
        });
    }

    private Optional<VkUser> shapeVkUser() {
        VkUser vkUser = new VkUser();
        vkUser.setId(683257828);
        vkUser.setClosed(false);
        vkUser.setFirstName("Ivan");
        vkUser.setLastName("Ivanov");
        vkUser.setOnline(Online.YES);
        vkUser.setCanAccessClosed(true);
        return Optional.of(vkUser);
    }

    private Optional<User> shapeUser() {
        User user = new User();
        user.setId(1L);
        user.setVersion(0);
        user.setVkId(683257828);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return Optional.of(user);
    }

    private Optional<Token> shapeToken() {
        Token token = new Token();
        token.setToken("b7945af1ce80f014b789e3f7695b9027d2dc9e2def15b7494191d654bcec80ac1b77eed5584e120dd929a");
        token.setUser(shapeUser().orElse(new User()));
        token.setUpdatedAt(new Date());
        return Optional.of(token);
    }

    private AddTaskDTO shapeAddTaskDto() {
        AddTaskDTO addTaskDTO = new AddTaskDTO();
        addTaskDTO.setVkUserId("sergdevguy");
        addTaskDTO.setOwnerVkId(683257828);
        addTaskDTO.setPeriod(2000L);
        return addTaskDTO;
    }

    private AddTaskDTO shapeInvalidAddTaskDto() {
        AddTaskDTO addTaskDTO = new AddTaskDTO();
        addTaskDTO.setVkUserId(null);
        addTaskDTO.setOwnerVkId(0);
        addTaskDTO.setPeriod(1L);
        return addTaskDTO;
    }

    private CancelTaskDTO shapeCancelTaskDto() {
        CancelTaskDTO cancelTaskDTO = new CancelTaskDTO();
        cancelTaskDTO.setVkUserId("sergdevguy");
        cancelTaskDTO.setOwnerVkId(683257828);
        return cancelTaskDTO;
    }

    private CancelTaskDTO shapeInvalidCancelTaskDto() {
        CancelTaskDTO cancelTaskDTO = new CancelTaskDTO();
        cancelTaskDTO.setVkUserId(null);
        cancelTaskDTO.setOwnerVkId(0);
        return cancelTaskDTO;
    }


    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
