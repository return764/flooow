package com.yutao.flooow.core.manager.scheduler

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
        scheduledFutures[task.name]?.also {
            it.cancel(true)
        }
    }

    fun schedule(task: ExecutableTask): ScheduledFuture<*>? {
        val feature = taskScheduler.schedule(task, task.triggerManager.getTrigger())
        scheduledFutures.put(task.name, feature!!)
        return feature
    }
}
