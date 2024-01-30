package com.yutao.flooow.core.manager.scheduler

import com.yutao.flooow.core.manager.ExecutableTask
import org.springframework.stereotype.Component

@Component
class ComposeTaskSchedulers(
    val taskSchedulers: List<TaskScheduler>
) {
    fun schedule(task: ExecutableTask) {
        cancel(task)
        taskSchedulers
            .filter { it.support() == task.type }
            .forEach { it.schedule(task) }
    }

    fun cancel(task: ExecutableTask) {
        taskSchedulers
            .filter { it.support() == task.type }
            .forEach { it.cancel(task) }
    }
}
