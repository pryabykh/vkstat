package com.pryabykh.vkstat.core.services;

import com.pryabykh.vkstat.core.db.Check;
import com.pryabykh.vkstat.core.dto.FetchAllRequestDTO;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import org.springframework.data.domain.Page;

public interface CheckService {
    Page<Check> fetchAll(FetchAllRequestDTO fetchAllRequestDTO) throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException;
}
