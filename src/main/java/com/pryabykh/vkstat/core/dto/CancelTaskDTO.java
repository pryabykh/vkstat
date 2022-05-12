package com.pryabykh.vkstat.core.dto;

import lombok.Data;

@Data
public class CancelTaskDTO {
    private String vkUserId;
    private int ownerVkId;
}
