package com.yutao.flooow.core.manager.registry

import com.yutao.flooow.core.TaskDefinition

interface TaskRegistry {
    fun register(definition: TaskDefinition)
    fun accept(definition: TaskDefinition): Boolean
}
