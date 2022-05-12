package com.pryabykh.vkstat.core.dto;

import lombok.Data;

import java.util.concurrent.ScheduledFuture;

@Data
public class CheckTaskDTO {
    private int ownerVkId;
    private int vkUserId;
    private long period;
    private ScheduledFuture<?> scheduledFuture;
}
