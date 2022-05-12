package com.pryabykh.vkstat.core.tasks;

public interface CheckTask {
    Runnable getRunnableFor(int ownerVkId, int vkUserId);
}
