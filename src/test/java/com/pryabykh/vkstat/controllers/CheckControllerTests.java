package com.pryabykh.vkstat.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pryabykh.vkstat.core.db.Check;
import com.pryabykh.vkstat.core.dto.FetchAllRequestDTO;
import com.pryabykh.vkstat.core.services.CheckService;
import com.pryabykh.vkstat.core.services.exceptions.InvalidDtoException;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import com.pryabykh.vkstat.http.controllers.CheckController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckController.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CheckControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckService checkService;

    @Test
    public void fetchAllPositive() throws Exception {
        Mockito.when(checkService.fetchAll(Mockito.any())).thenReturn(
                PageableExecutionUtils.getPage(
                        List.of(new Check(), new Check()),
                        PageRequest.of(1, 10),
                        () -> 10)
        );

        mockMvc.perform(post("/v1/checks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(shapeFetchAllDataJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void fetchAllBadRequest() throws Exception {
        Mockito.when(checkService.fetchAll(Mockito.any())).thenThrow(InvalidDtoException.class);

        mockMvc.perform(post("/v1/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeFetchAllDataJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void fetchAllInternalServerError() throws Exception {
        Mockito.when(checkService.fetchAll(Mockito.any())).thenThrow(RequestToVkApiFailedException.class);

        mockMvc.perform(post("/v1/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shapeFetchAllDataJson()))
                .andExpect(status().is5xxServerError());
    }

    private String shapeFetchAllDataJson() throws JsonProcessingException {
        FetchAllRequestDTO fetchAllRequestDTO = new FetchAllRequestDTO();
        fetchAllRequestDTO.setOwnerVkId(683257828);
        fetchAllRequestDTO.setVkUserId("sergdevguy");
        fetchAllRequestDTO.setPage(1);
        fetchAllRequestDTO.setSize(10);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(fetchAllRequestDTO);
    }
}
