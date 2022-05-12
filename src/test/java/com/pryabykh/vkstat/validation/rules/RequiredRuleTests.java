package com.pryabykh.vkstat.validation.rules;

import com.pryabykh.vkstat.vendor.validation.ValidationRuleResult;
import com.pryabykh.vkstat.vendor.validation.rules.RequiredRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RequiredRuleTests {
    @Test
    public void positive() {
        RequiredRule requiredRule = new RequiredRule();
        ValidationRuleResult validationRuleResult = requiredRule.check("Some value");
        Assertions.assertFalse(validationRuleResult.hasErrors());
    }

    @Test
    public void negativeEmptyValue() {
        RequiredRule requiredRule = new RequiredRule();
        ValidationRuleResult validationRuleResult = requiredRule.check("");
        Assertions.assertTrue(validationRuleResult.hasErrors());
    }

    @Test
    public void negativeNullValue() {
        RequiredRule requiredRule = new RequiredRule();
        ValidationRuleResult validationRuleResult = requiredRule.check(null);
        Assertions.assertTrue(validationRuleResult.hasErrors());
    }
}
