package com.pryabykh.vkstat.core.services;

import com.pryabykh.vkstat.core.dto.AddTaskDTO;
import com.pryabykh.vkstat.core.dto.CancelTaskDTO;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;

public interface TaskService {
    boolean addTask(AddTaskDTO addTaskDTO) throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException;
    boolean cancelTask(CancelTaskDTO cancelTaskDTO) throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException;
}
