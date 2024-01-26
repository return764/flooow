package com.yutao.flooow.core.manager

import com.yutao.flooow.core.TaskDefinition


interface TaskBuilder {
    fun build(taskDefinition: TaskDefinition): ExecutableTask
}
