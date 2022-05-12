package com.pryabykh.vkstat.core.dto;

import lombok.Data;

@Data
public class AddTaskDTO {
    private String vkUserId;
    private int ownerVkId;
    private Long period;
}
