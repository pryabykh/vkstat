package com.pryabykh.vkstat.validation.rules;

import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.vendor.validation.ValidationRuleResult;
import com.pryabykh.vkstat.vendor.validation.rules.MinRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class MinRuleTests {
    private final MinRule minRule = new MinRule();

    @BeforeEach
    public void beforeEach() {
        String minLength = "3";
        minRule.setMinLength(minLength);
    }

    @Test
    public void positive() throws ValidationErrorException {
        ValidationRuleResult validationRuleResult = minRule.check("Some value");
        Assertions.assertFalse(validationRuleResult.hasErrors());
    }

    @Test
    public void lengthLessThanMinValue() throws ValidationErrorException {
        ValidationRuleResult validationRuleResult = minRule.check("Aa");
        Assertions.assertTrue(validationRuleResult.hasErrors());
    }

    @Test
    public void targetIsNull() throws ValidationErrorException {
        ValidationRuleResult validationRuleResult = minRule.check(null);
        Assertions.assertFalse(validationRuleResult.hasErrors());
    }

    @Test
    public void minLengthIsNull() {
        minRule.setMinLength(null);
        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            minRule.check("Some value");
        });
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    public void minLengthIsEmpty() {
        minRule.setMinLength("");
        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            minRule.check("Some value");
        });
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    public void minLengthIsNotANumber() {
        minRule.setMinLength("B1");
        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            minRule.check("Some value");
        });
        Assertions.assertNotNull(exception.getMessage());
    }
}
