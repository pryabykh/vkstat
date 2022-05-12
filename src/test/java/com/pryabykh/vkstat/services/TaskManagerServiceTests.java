package com.pryabykh.vkstat.services;

import com.pryabykh.vkstat.core.dto.AddTaskDTO;
import com.pryabykh.vkstat.core.dto.CheckTaskDTO;
import com.pryabykh.vkstat.core.services.TaskManagerService;
import com.pryabykh.vkstat.core.tasks.CheckTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TaskManagerServiceTests {
    private TaskManagerService taskManagerService;
    @MockBean
    private CheckTask checkTask;
    private Integer count = 0;

    @Test
    public void addAndCancelTask() throws InterruptedException {
        Mockito.when(checkTask.getRunnableFor(Mockito.anyInt(), Mockito.anyInt())).thenReturn(() -> count++);
        CheckTaskDTO checkTaskDTO = shapeCheckTaskDto();
        boolean run = taskManagerService.run(checkTaskDTO);
        Assertions.assertTrue(run);
        Thread.sleep(2000L);
        Assertions.assertTrue(count > 0);
        taskManagerService.cancel(checkTaskDTO.getVkUserId(), checkTaskDTO.getOwnerVkId());
        int finalCount = count;
        Thread.sleep(2000L);
        Assertions.assertEquals(finalCount, count);
    }

    private CheckTaskDTO shapeCheckTaskDto() {
        CheckTaskDTO task = new CheckTaskDTO();
        task.setPeriod(1000);
        task.setOwnerVkId(1);
        task.setVkUserId(2);
        return task;
    }

    @Autowired
    public void setTaskManagerService(TaskManagerService taskManagerService) {
        this.taskManagerService = taskManagerService;
    }
}
