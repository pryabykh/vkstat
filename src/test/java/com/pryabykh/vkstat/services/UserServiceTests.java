package com.pryabykh.vkstat.services;

import com.pryabykh.vkstat.core.db.Token;
import com.pryabykh.vkstat.core.db.User;
import com.pryabykh.vkstat.core.db.repositories.TokenRepository;
import com.pryabykh.vkstat.core.db.repositories.UserRepository;
import com.pryabykh.vkstat.core.dto.RegisteredUserDTO;
import com.pryabykh.vkstat.core.dto.RegistrationDataDTO;
import com.pryabykh.vkstat.core.services.UserService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.vk.VkService;
import com.pryabykh.vkstat.core.vk.ds.AuthResult;
import com.pryabykh.vkstat.core.vk.exceptions.VkAuthErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {
    private UserService userService;
    @MockBean
    private VkService vkService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TokenRepository tokenRepository;
    @MockBean
    private Token token;

//    @Test
    public void register() throws Exception {
        RegistrationDataDTO registrationDataDTO = new RegistrationDataDTO();
        registrationDataDTO.setCodeToGetToken("98d91fe398627701aa");
        RegisteredUserDTO registeredUser = userService.register(registrationDataDTO);
        System.out.println(registeredUser);
    }

    @Test
    public void registerPositive() throws Exception {
        Mockito.when(vkService.auth(Mockito.any())).thenReturn(shapeAuthResult());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(shapeUser());
        Mockito.when(userRepository.findByVkId(Mockito.anyInt())).thenReturn(Optional.of(shapeUser()));
        Mockito.when(tokenRepository.save(Mockito.any())).thenReturn(shapeToken());
        Mockito.when(tokenRepository.findByUserId(Mockito.anyLong())).thenReturn(Optional.of(shapeToken()));
        Mockito.when(token.getUser()).thenReturn(shapeUser());

        RegistrationDataDTO registrationDataDTO = new RegistrationDataDTO();
        registrationDataDTO.setCodeToGetToken("98d91fe398627701aa");
        RegisteredUserDTO registeredUser = userService.register(registrationDataDTO);
        System.out.println(registeredUser);
    }

    @Test
    public void registerInvalidCode() throws VkAuthErrorException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        Mockito.when(vkService.auth(Mockito.any())).thenReturn(shapeAuthResult());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(shapeUser());
        Mockito.when(userRepository.findByVkId(Mockito.anyInt())).thenReturn(Optional.of(shapeUser()));
        Mockito.when(tokenRepository.save(Mockito.any())).thenReturn(shapeToken());
        Mockito.when(tokenRepository.findByUserId(Mockito.anyLong())).thenReturn(Optional.of(shapeToken()));

        RegistrationDataDTO registrationDataDTO = new RegistrationDataDTO();
        registrationDataDTO.setCodeToGetToken(null);
        InvalidDtoException exception = assertThrows(InvalidDtoException.class, () -> {
            RegisteredUserDTO registeredUser = userService.register(registrationDataDTO);
        });
    }

    private AuthResult shapeAuthResult() {
        AuthResult authResult = new AuthResult();
        authResult.setUserId(683257828);
        authResult.setAccessToken("42086410a3104646e36e749ac45a3b3822d9dbc278e4223256365de18709c1485d21b8af9237cde48c157");
        authResult.setExpiresIn(0);
        return authResult;
    }

    private User shapeUser() {
        User user = new User();
        user.setId(1L);
        user.setVersion(0);
        user.setVkId(683257828);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return user;
    }

    private Token shapeToken() {
        Token token = new Token();
        token.setId(1L);
        token.setToken("42086410a3104646e36e749ac45a3b3822d9dbc278e4223256365de18709c1485d21b8af9237cde48c157");
        token.setUser(shapeUser());
        token.setCreatedAt(new Date());
        token.setUpdatedAt(new Date());
        return token;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
