package com.yutao.flooow.core.manager.scheduler

import com.yutao.flooow.core.model.ExecutableTask
import com.yutao.flooow.enums.TaskType
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import java.util.concurrent.ScheduledFuture

@Component
class TimerTaskScheduler(
    val taskScheduler: ThreadPoolTaskScheduler
) : TaskScheduler {
    val scheduledFutures = mutableMapOf<String, ScheduledFuture<*>>()

    override fun cancel(task: ExecutableTask) {
        scheduledFutures[task.identify.fileName]?.also {
            it.cancel(true)
        }
    }

    override fun support(): TaskType {
        return TaskType.TIMER
    }

    override fun schedule(task: ExecutableTask) {
        val feature = taskScheduler.schedule(task, task.triggerManager.getTrigger())
        scheduledFutures.put(task.identify.fileName, feature!!)
    }
}
