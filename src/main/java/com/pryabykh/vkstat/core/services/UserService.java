package com.pryabykh.vkstat.core.services;

import com.pryabykh.vkstat.core.dto.RegisteredUserDTO;
import com.pryabykh.vkstat.core.dto.RegistrationDataDTO;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.exceptions.VkAuthErrorException;

public interface UserService {
    RegisteredUserDTO register(RegistrationDataDTO registrationDataDTO) throws Exception;
}
