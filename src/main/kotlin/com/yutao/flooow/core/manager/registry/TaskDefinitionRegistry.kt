package com.yutao.flooow.core.manager.registry

import com.yutao.flooow.core.model.TaskDefinition
import com.yutao.flooow.core.model.TaskIdentifier

interface TaskDefinitionRegistry {
    fun registerTaskDefinition(name: TaskIdentifier, definition: TaskDefinition)
    fun unRegisterTaskDefinition(name: TaskIdentifier)
    fun getTaskDefinition(name: TaskIdentifier): TaskDefinition
}
