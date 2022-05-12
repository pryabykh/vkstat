package com.pryabykh.vkstat.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pryabykh.vkstat.core.db.Check;
import com.pryabykh.vkstat.core.db.Token;
import com.pryabykh.vkstat.core.db.User;
import com.pryabykh.vkstat.core.db.repositories.CheckRepository;
import com.pryabykh.vkstat.core.db.repositories.TokenRepository;
import com.pryabykh.vkstat.core.db.repositories.UserRepository;
import com.pryabykh.vkstat.core.dto.FetchAllRequestDTO;
import com.pryabykh.vkstat.core.services.CheckService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CheckServiceTests {
    private CheckService checkService;
    @MockBean
    private VkService vkService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TokenRepository tokenRepository;
    @MockBean
    private CheckRepository checkRepository;

    @Test
    public void fetchAllPositive() throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        Mockito.when(vkService.getUser(Mockito.anyString(), Mockito.anyString())).thenReturn(shapeVkUser());
        Mockito.when(userRepository.findByVkId(Mockito.anyInt())).thenReturn(shapeUser());
        Mockito.when(tokenRepository.findByUserId(Mockito.anyLong())).thenReturn(shapeToken());
        Mockito.when(checkRepository.findByOwnerIdAndVkId(Mockito.anyLong(), Mockito.anyInt(), Mockito.any()))
                .thenReturn(shapePageOfChecks());
        Page<Check> checks = checkService.fetchAll(shapeFetchAllDto());
    }

    @Test
    public void fetchAllInvalidDto() throws RequestToVkApiFailedException, ValidationErrorException, NoSuchFieldValidationException, InvalidDtoException {
        Mockito.when(vkService.getUser(Mockito.anyString(), Mockito.anyString())).thenReturn(shapeVkUser());
        Mockito.when(userRepository.findByVkId(Mockito.anyInt())).thenReturn(shapeUser());
        Mockito.when(tokenRepository.findByUserId(Mockito.anyLong())).thenReturn(shapeToken());
        Mockito.when(checkRepository.findByOwnerIdAndVkId(Mockito.anyLong(), Mockito.anyInt(), Mockito.any()))
                .thenReturn(shapePageOfChecks());
        Assertions.assertThrows(InvalidDtoException.class, () -> {
            checkService.fetchAll(shapeInvalidFetchAllDto());
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

    private Page<Check> shapePageOfChecks() {
        return PageableExecutionUtils.getPage(
                List.of(new Check(), new Check()),
                PageRequest.of(1, 10),
                () -> 1
        );
    }

    private FetchAllRequestDTO shapeFetchAllDto() {
        FetchAllRequestDTO fetchAllRequestDTO = new FetchAllRequestDTO();
        fetchAllRequestDTO.setOwnerVkId(683257828);
        fetchAllRequestDTO.setVkUserId("sergdevguy");
        fetchAllRequestDTO.setPage(1);
        fetchAllRequestDTO.setSize(10);
        return fetchAllRequestDTO;
    }

    private FetchAllRequestDTO shapeInvalidFetchAllDto() {
        FetchAllRequestDTO fetchAllRequestDTO = new FetchAllRequestDTO();
        fetchAllRequestDTO.setOwnerVkId(0);
        fetchAllRequestDTO.setVkUserId(null);
        fetchAllRequestDTO.setPage(1);
        fetchAllRequestDTO.setSize(10);
        return fetchAllRequestDTO;
    }

    @Autowired
    public void setCheckService(CheckService checkService) {
        this.checkService = checkService;
    }
}
