package com.yutao.flooow.core.scheduler.registry

import com.yutao.flooow.core.TaskDefinition

interface TaskRegistry {
    fun register(definition: TaskDefinition)
    fun accept(definition: TaskDefinition): Boolean
}
