package com.pryabykh.vkstat.vendor.validation;

import com.pryabykh.vkstat.core.validation.exceptions.NoSuchFieldValidationException;
import com.pryabykh.vkstat.core.validation.exceptions.ValidationErrorException;
import com.pryabykh.vkstat.core.validation.ValidationResult;
import com.pryabykh.vkstat.vendor.validation.rules.RulesSimpleFactoryImpl;
import com.pryabykh.vkstat.vendor.validation.rules.ValidationRule;

import java.lang.reflect.Field;
import java.util.Map;

import static java.lang.String.format;

public enum ValidationUtilImpl implements ValidationUtil {
    INSTANCE;
    private final RulesSimpleFactory rulesSimpleFactory = new RulesSimpleFactoryImpl();

    @Override
    public ValidationResult check(Object target, Map<String, String> validationRules)
            throws ValidationErrorException, NoSuchFieldValidationException {
        if (target == null) {
            throw new ValidationErrorException("Validation target cannot be null");
        }
        if (validationRules == null) {
            throw new ValidationErrorException("Validation rules cannot be null");
        }
        ValidationResult validationResult = new ValidationResultImpl();
        for (Map.Entry<String, String> entry : validationRules.entrySet()) {
            String fieldName = entry.getKey();
            if (fieldName == null) {
                throw new ValidationErrorException("Field for validation cannot be null");
            }
            Object fieldValue = fetchFieldValueByName(fieldName, target);
            String rulesString = entry.getValue();
            if (rulesString == null) {
                throw new ValidationErrorException(format("Validation rules are null for field %s", fieldName));
            }
            String[] rules = rulesString.split("\\|");
            for (String rule : rules) {
                ValidationRule validationRule = rulesSimpleFactory.getRule(rule);
                ValidationRuleResult validationRuleResult = validationRule.check(fieldValue);
                if (validationRuleResult.hasErrors()) {
                    validationResult.addError(validationRuleResult.getError());
                }
            }
        }
        return validationResult;
    }

    private Object fetchFieldValueByName(String fieldName, Object target)
            throws NoSuchFieldValidationException, ValidationErrorException {
        Field field;
        try {
            field = target.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new NoSuchFieldValidationException(format("Cannot find field [%s] in object %s", fieldName, target));
        }
        field.setAccessible(true);
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new ValidationErrorException(format("Cannot access field [%s] in object %s", fieldName, target));
        }
    }
}
