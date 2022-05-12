package com.pryabykh.vkstat.core.services;

import com.pryabykh.vkstat.core.dto.CheckTaskDTO;
import com.pryabykh.vkstat.core.tasks.CheckTask;

public interface TaskManagerService {
    boolean run(CheckTaskDTO checkTaskDTO);
    boolean cancel(int vkUserId, int ownerVkId);
    boolean isRunning(int vkUserId, int ownerVkId);
}
