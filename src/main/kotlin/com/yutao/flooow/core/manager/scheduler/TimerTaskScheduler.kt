package com.yutao.flooow.core.manager.scheduler

import com.yutao.flooow.core.TaskIdentifier
import com.yutao.flooow.core.manager.ExecutableTask
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import java.util.concurrent.ScheduledFuture

@Component
class TimerTaskScheduler(
    val taskScheduler: ThreadPoolTaskScheduler
) {
    val scheduledFutures = mutableMapOf<String, ScheduledFuture<*>>()

    fun cancel(task: ExecutableTask) {
        scheduledFutures[task.identify.fileName]?.also {
            it.cancel(true)
        }
    }

    fun schedule(task: ExecutableTask) {
        cancel(task)
        val feature = taskScheduler.schedule(task, task.triggerManager.getTrigger())
        scheduledFutures.put(task.identify.fileName, feature!!)
    }
}
