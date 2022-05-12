package com.pryabykh.vkstat.validation.rules;

import com.pryabykh.vkstat.vendor.validation.ValidationRuleResult;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.vendor.validation.rules.MaxRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class MaxRuleTests {
    private final MaxRule maxRule = new MaxRule();

    @BeforeEach
    public void beforeEach() {
        String maxLength = "10";
        maxRule.setMaxLength(maxLength);
    }

    @Test
    public void positive() throws ValidationErrorException {
        ValidationRuleResult validationRuleResult = maxRule.check("Some value");
        Assertions.assertFalse(validationRuleResult.hasErrors());
    }

    @Test
    public void lengthMoreThanMaxValue() throws ValidationErrorException {
        ValidationRuleResult validationRuleResult = maxRule.check("Some value1");
        Assertions.assertTrue(validationRuleResult.hasErrors());
    }

    @Test
    public void targetIsNull() throws ValidationErrorException {
        ValidationRuleResult validationRuleResult = maxRule.check(null);
        Assertions.assertFalse(validationRuleResult.hasErrors());
    }

    @Test
    public void maxLengthIsNull() {
        maxRule.setMaxLength(null);
        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            maxRule.check("Some value");
        });
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    public void maxLengthIsEmpty() {
        maxRule.setMaxLength("");
        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            maxRule.check("Some value");
        });
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    public void maxLengthIsNotANumber() {
        maxRule.setMaxLength("B1");
        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            maxRule.check("Some value");
        });
        Assertions.assertNotNull(exception.getMessage());
    }
}
