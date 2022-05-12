package com.pryabykh.vkstat.vendor.tasks;

import com.pryabykh.vkstat.core.dto.CheckTaskDTO;
import com.pryabykh.vkstat.core.services.TaskManagerService;
import com.pryabykh.vkstat.core.tasks.CheckTask;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Service
public class TaskManagerServiceImpl implements TaskManagerService {
    private final CheckTask checkTask;
    private final TaskScheduler taskScheduler;
    private final List<CheckTaskDTO> tasks = new ArrayList<>();

    public TaskManagerServiceImpl(CheckTask checkTask, TaskScheduler taskScheduler) {
        this.checkTask = checkTask;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public boolean run(CheckTaskDTO checkTaskDTO) {
        Runnable runnable = checkTask.getRunnableFor(checkTaskDTO.getOwnerVkId(), checkTaskDTO.getVkUserId());
        Trigger trigger = new PeriodicTrigger(checkTaskDTO.getPeriod());
        ScheduledFuture<?> schedule = taskScheduler.schedule(runnable, trigger);
        checkTaskDTO.setScheduledFuture(schedule);
        tasks.add(checkTaskDTO);
        return true;
    }

    @Override
    public boolean cancel(int vkUserId, int ownerVkId) {
        Optional<CheckTaskDTO> task = tasks.stream().filter(t -> {
            return t.getOwnerVkId() == ownerVkId && t.getVkUserId() == vkUserId;
        }).findFirst();
        if (task.isPresent()) {
            ScheduledFuture<?> scheduledFuture = task.get().getScheduledFuture();
            scheduledFuture.cancel(false);
        }
        return true;
    }

    @Override
    public boolean isRunning(int vkUserId, int ownerVkId) {
        return tasks.stream().anyMatch(t -> {
            return t.getOwnerVkId() == ownerVkId && t.getVkUserId() == vkUserId;
        });
    }
}
