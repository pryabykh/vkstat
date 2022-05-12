package com.pryabykh.vkstat.app.services;

import com.pryabykh.vkstat.app.validation.FetchAllRequestValidationStrategy;
import com.pryabykh.vkstat.core.db.Check;
import com.pryabykh.vkstat.core.db.Token;
import com.pryabykh.vkstat.core.db.User;
import com.pryabykh.vkstat.core.db.repositories.CheckRepository;
import com.pryabykh.vkstat.core.db.repositories.TokenRepository;
import com.pryabykh.vkstat.core.db.repositories.UserRepository;
import com.pryabykh.vkstat.core.dto.FetchAllRequestDTO;
import com.pryabykh.vkstat.core.services.CheckService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.ValidationResult;
import com.pryabykh.vkstat.core.validation.ValidationService;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.VkService;
import com.pryabykh.vkstat.core.vk.ds.VkUser;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CheckServiceImpl implements CheckService {
    private final CheckRepository checkRepository;
    private final VkService vkService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ValidationService validationService;

    public CheckServiceImpl(CheckRepository checkRepository, VkService vkService, UserRepository userRepository, TokenRepository tokenRepository, ValidationService validationService) {
        this.checkRepository = checkRepository;
        this.vkService = vkService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.validationService = validationService;
    }

    @Override
    public Page<Check> fetchAll(FetchAllRequestDTO fetchAllRequestDTO) throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        ValidationResult validationResult = validationService.validate(fetchAllRequestDTO, FetchAllRequestValidationStrategy.INSTANCE);
        if (validationResult.hasErrors()) {
            throw new InvalidDtoException("FetchAllRequestDTO has validation errors");
        }
        User owner = userRepository.findByVkId(fetchAllRequestDTO.getOwnerVkId()).orElseThrow(IllegalArgumentException::new);
        Token token = tokenRepository.findByUserId(owner.getId()).orElseThrow(IllegalArgumentException::new);
        VkUser targetUser = vkService.getUser(fetchAllRequestDTO.getVkUserId(), token.getToken()).orElseThrow(IllegalArgumentException::new);
        PageRequest pageRequest = PageRequest.of(fetchAllRequestDTO.getPage(), fetchAllRequestDTO.getSize());
        return checkRepository.findByOwnerIdAndVkId(owner.getId(), targetUser.getId(), pageRequest);
    }
}
