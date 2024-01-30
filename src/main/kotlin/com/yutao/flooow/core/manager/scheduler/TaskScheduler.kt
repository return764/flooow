package com.yutao.flooow.core.manager.scheduler

import com.yutao.flooow.core.manager.ExecutableTask
import com.yutao.flooow.enums.TaskType

interface TaskScheduler {
    fun schedule(task: ExecutableTask)
    fun cancel(task: ExecutableTask)
    fun support(): TaskType
}
