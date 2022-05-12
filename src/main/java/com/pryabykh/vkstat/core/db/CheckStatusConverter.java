package com.pryabykh.vkstat.core.db;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CheckStatusConverter implements AttributeConverter<CheckStatus, String> {
    @Override
    public String convertToDatabaseColumn(CheckStatus checkStatus) {
        if (checkStatus == null) {
            return null;
        }
        return checkStatus.name();
    }

    @Override
    public CheckStatus convertToEntityAttribute(String activityStatus) {
        if (activityStatus == null) {
            return null;
        }
        return CheckStatus.valueOf(activityStatus);
    }
}
