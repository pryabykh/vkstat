package com.pryabykh.vkstat.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pryabykh.vkstat.core.dto.RegistrationDataDTO;
import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.validation.ValidationResult;
import com.pryabykh.vkstat.vendor.validation.ValidationUtil;
import com.pryabykh.vkstat.vendor.validation.ValidationUtilImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class ValidationUtilTests {
    private ResourceLoader resourceLoader;
    private ObjectMapper objectMapper;
    private final ValidationUtil validationUtil = ValidationUtilImpl.INSTANCE;
    private static final String JSON_PATH_FOR_NEGATIVE = "classpath:jsonDto/registrationData/negative/";
    private static final String JSON_PATH_FOR_POSITIVE = "classpath:jsonDto/registrationData/positive/";

    @ParameterizedTest
    @ValueSource(strings = {"registrationData.json"})
    public void positive(String jsonFileName) throws IOException, ValidationErrorException, NoSuchFieldValidationException {
        String jsonFilePath = JSON_PATH_FOR_POSITIVE + jsonFileName;
        RegistrationDataDTO registrationDataDTO = objectMapper.readValue(
                resourceLoader.getResource(jsonFilePath).getFile(), RegistrationDataDTO.class
        );
        Map<String, String> rules = new HashMap<>();
        rules.put("codeToGetToken", "required|max:255|min:3");
        ValidationResult validationResult = validationUtil.check(registrationDataDTO, rules);
        Assertions.assertFalse(validationResult.hasErrors());
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
        Map<String, String> rules = new HashMap<>();
        rules.put("codeToGetToken", "required|max:255");
        ValidationResult validationResult = validationUtil.check(registrationDataDTO, rules);
        Assertions.assertTrue(validationResult.hasErrors());
    }

    @Test
    public void validationTargetIsNull() {
        Map<String, String> rules = new HashMap<>();
        rules.put("codeToGetToken", "required|max:255");
        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            validationUtil.check(null, rules);
        });
        Assertions.assertNotNull(exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"registrationData.json"})
    public void validationRulesIsNull(String jsonFileName) throws IOException {
        String jsonFilePath = JSON_PATH_FOR_POSITIVE + jsonFileName;
        RegistrationDataDTO registrationDataDTO = objectMapper.readValue(
                resourceLoader.getResource(jsonFilePath).getFile(), RegistrationDataDTO.class
        );
        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            validationUtil.check(registrationDataDTO, null);
        });
        Assertions.assertNotNull(exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"registrationData.json"})
    public void nonexistentFieldForValidation(String jsonFileName) throws IOException {
        String jsonFilePath = JSON_PATH_FOR_POSITIVE + jsonFileName;
        RegistrationDataDTO registrationDataDTO = objectMapper.readValue(
                resourceLoader.getResource(jsonFilePath).getFile(), RegistrationDataDTO.class
        );
        Map<String, String> rules = new HashMap<>();
        rules.put("nonexistentField", "required|max:255");
        NoSuchFieldValidationException exception = assertThrows(NoSuchFieldValidationException.class, () -> {
            validationUtil.check(registrationDataDTO, rules);
        });
        Assertions.assertNotNull(exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"registrationData.json"})
    public void fieldForValidationIsNull(String jsonFileName) throws IOException {
        String jsonFilePath = JSON_PATH_FOR_POSITIVE + jsonFileName;
        RegistrationDataDTO registrationDataDTO = objectMapper.readValue(
                resourceLoader.getResource(jsonFilePath).getFile(), RegistrationDataDTO.class
        );
        Map<String, String> rules = new HashMap<>();
        rules.put(null, "required|max:255");
        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            validationUtil.check(registrationDataDTO, rules);
        });
        Assertions.assertNotNull(exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"registrationData.json"})
    public void validationRuleIsNull(String jsonFileName) throws IOException {
        String jsonFilePath = JSON_PATH_FOR_POSITIVE + jsonFileName;
        RegistrationDataDTO registrationDataDTO = objectMapper.readValue(
                resourceLoader.getResource(jsonFilePath).getFile(), RegistrationDataDTO.class
        );
        Map<String, String> rules = new HashMap<>();
        rules.put("codeToGetToken", null);
        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            validationUtil.check(registrationDataDTO, rules);
        });
        Assertions.assertNotNull(exception.getMessage());
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
