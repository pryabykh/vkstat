package com.pryabykh.vkstat.app.services;

import com.pryabykh.vkstat.app.validation.RegistrationDataValidationStrategy;
import com.pryabykh.vkstat.core.db.Token;
import com.pryabykh.vkstat.core.db.User;
import com.pryabykh.vkstat.core.db.repositories.TokenRepository;
import com.pryabykh.vkstat.core.db.repositories.UserRepository;
import com.pryabykh.vkstat.core.dto.RegisteredUserDTO;
import com.pryabykh.vkstat.core.dto.RegistrationDataDTO;
import com.pryabykh.vkstat.core.services.UserService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.ValidationResult;
import com.pryabykh.vkstat.core.validation.ValidationService;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.VkService;
import com.pryabykh.vkstat.core.vk.ds.AuthResult;
import com.pryabykh.vkstat.core.vk.exceptions.VkAuthErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {
    private final ValidationService validationService;
    private final VkService vkService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public UserServiceImpl(ValidationService validationService, VkService vkService, UserRepository userRepository, TokenRepository tokenRepository) {
        this.validationService = validationService;
        this.vkService = vkService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public RegisteredUserDTO register(RegistrationDataDTO registrationDataDTO) throws ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException, VkAuthErrorException {
        ValidationResult validationResult = validationService.validate(
                registrationDataDTO, RegistrationDataValidationStrategy.INSTANCE
        );
        if (validationResult.hasErrors()) {
            throw new InvalidDtoException("RegistrationDataDTO has validation errors");
        }
        AuthResult authResult = vkService.auth(registrationDataDTO.getCodeToGetToken());
        return shapeRegisteredUserDto(registerUser(authResult));
    }

    private RegisteredUserDTO shapeRegisteredUserDto(Token token) {
        RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO();
        User registeredUser = token.getUser();
        registeredUserDTO.setId(registeredUser.getId());
        registeredUserDTO.setVkUserId(registeredUser.getVkId());
        registeredUserDTO.setVkToken(token.getToken());
        return registeredUserDTO;
    }

    private Token registerUser(AuthResult authResult) {
        User user = userRepository.findByVkId(authResult.getUserId()).orElse(new User());
        user.setVkId(authResult.getUserId());
        Token token;
        if (user.getId() == null) {
            token = new Token();
        } else {
            token = tokenRepository.findByUserId(user.getId()).orElse(new Token());
        }
        token.setToken(authResult.getAccessToken());
        token.setUser(userRepository.save(user));
        return tokenRepository.save(token);
    }
}
