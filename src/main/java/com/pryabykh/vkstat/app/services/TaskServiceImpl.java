package com.pryabykh.vkstat.app.services;

import com.pryabykh.vkstat.app.validation.AddTaskValidationStrategy;
import com.pryabykh.vkstat.app.validation.CancelTaskValidationStrategy;
import com.pryabykh.vkstat.core.db.Token;
import com.pryabykh.vkstat.core.db.User;
import com.pryabykh.vkstat.core.db.repositories.TokenRepository;
import com.pryabykh.vkstat.core.db.repositories.UserRepository;
import com.pryabykh.vkstat.core.dto.AddTaskDTO;
import com.pryabykh.vkstat.core.dto.CancelTaskDTO;
import com.pryabykh.vkstat.core.dto.CheckTaskDTO;
import com.pryabykh.vkstat.core.services.TaskManagerService;
import com.pryabykh.vkstat.core.services.TaskService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.ValidationResult;
import com.pryabykh.vkstat.core.validation.ValidationService;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.VkService;
import com.pryabykh.vkstat.core.vk.ds.VkUser;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import org.springframework.stereotype.Service;


@Service
public class TaskServiceImpl implements TaskService {
    private final TaskManagerService taskManagerService;
    private final VkService vkService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ValidationService validationService;

    public TaskServiceImpl(TaskManagerService taskManagerService, VkService vkService, UserRepository userRepository, TokenRepository tokenRepository, ValidationService validationService) {
        this.taskManagerService = taskManagerService;
        this.vkService = vkService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.validationService = validationService;
    }

    @Override
    public boolean addTask(AddTaskDTO addTaskDTO) throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        ValidationResult validationResult = validationService.validate(addTaskDTO, AddTaskValidationStrategy.INSTANCE);
        if (validationResult.hasErrors()) {
            throw new InvalidDtoException("AddTaskDTO has validation errors");
        }
        User owner = userRepository.findByVkId(addTaskDTO.getOwnerVkId()).orElseThrow(IllegalArgumentException::new);
        Token token = tokenRepository.findByUserId(owner.getId()).orElseThrow(IllegalArgumentException::new);
        VkUser targetUser = vkService.getUser(addTaskDTO.getVkUserId(), token.getToken()).orElseThrow(IllegalArgumentException::new);
        if (taskManagerService.isRunning(targetUser.getId(), addTaskDTO.getOwnerVkId())) {
            return false;
        }
        CheckTaskDTO task = new CheckTaskDTO();
        task.setPeriod(addTaskDTO.getPeriod());
        task.setOwnerVkId(addTaskDTO.getOwnerVkId());
        task.setVkUserId(targetUser.getId());
        return taskManagerService.run(task);
    }

    @Override
    public boolean cancelTask(CancelTaskDTO cancelTaskDTO) throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        ValidationResult validationResult = validationService.validate(cancelTaskDTO, CancelTaskValidationStrategy.INSTANCE);
        if (validationResult.hasErrors()) {
            throw new InvalidDtoException("CancelTaskDTO has validation errors");
        }
        User owner = userRepository.findByVkId(cancelTaskDTO.getOwnerVkId()).orElseThrow(IllegalArgumentException::new);
        Token token = tokenRepository.findByUserId(owner.getId()).orElseThrow(IllegalArgumentException::new);
        VkUser targetUser = vkService.getUser(cancelTaskDTO.getVkUserId(), token.getToken()).orElseThrow(IllegalArgumentException::new);
        return taskManagerService.cancel(targetUser.getId(), cancelTaskDTO.getOwnerVkId());
    }
}
