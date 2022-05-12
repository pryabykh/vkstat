package com.pryabykh.vkstat.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pryabykh.vkstat.core.dto.RegistrationDataDTO;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.app.validation.RegistrationDataValidationStrategy;
import com.pryabykh.vkstat.core.validation.ValidationResult;
import com.pryabykh.vkstat.core.validation.ValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;


@SpringBootTest
@ActiveProfiles("test")
public class RegistrationDataDtoTests {
    private ResourceLoader resourceLoader;
    private ObjectMapper objectMapper;
    private ValidationService validationService;
    private static final String JSON_PATH_FOR_NEGATIVE = "classpath:jsonDto/registrationData/negative/";
    private static final String JSON_PATH_FOR_POSITIVE = "classpath:jsonDto/registrationData/positive/";

    @ParameterizedTest
    @ValueSource(strings = {"registrationData.json"})
    public void positive(String jsonFileName) throws IOException, ValidationErrorException, NoSuchFieldValidationException {
        String jsonFilePath = JSON_PATH_FOR_POSITIVE + jsonFileName;
        RegistrationDataDTO registrationDataDTO = objectMapper.readValue(
                resourceLoader.getResource(jsonFilePath).getFile(), RegistrationDataDTO.class
        );
        validationService.validate(registrationDataDTO, RegistrationDataValidationStrategy.INSTANCE);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "codeToGetTokenIsEmpty.json",
            "codeToGetTokenIsMoreThan255Chars.json",
            "codeToGetTokenIsNull.json"
    })
    public void negative(String jsonFileName) throws IOException, ValidationErrorException, NoSuchFieldValidationException {
        String jsonFilePath = JSON_PATH_FOR_NEGATIVE + jsonFileName;
        RegistrationDataDTO registrationDataDTO = objectMapper.readValue(
                resourceLoader.getResource(jsonFilePath).getFile(), RegistrationDataDTO.class
        );
        ValidationResult validationResult = validationService.validate(
                registrationDataDTO, RegistrationDataValidationStrategy.INSTANCE
        );
        Assertions.assertTrue(validationResult.hasErrors());
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setValidationService(ValidationService validationService) {
        this.validationService = validationService;
    }
}
