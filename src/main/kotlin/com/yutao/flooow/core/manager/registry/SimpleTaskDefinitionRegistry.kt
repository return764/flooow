package com.yutao.flooow.core.manager.registry

import com.yutao.flooow.core.exception.TaskException
import com.yutao.flooow.core.model.TaskDefinition
import com.yutao.flooow.core.model.TaskIdentifier
import org.springframework.stereotype.Component

@Component
open class SimpleTaskDefinitionRegistry: TaskDefinitionRegistry {
    private val map = mutableMapOf<TaskIdentifier, TaskDefinition>()

    override fun registerTaskDefinition(name: TaskIdentifier, definition: TaskDefinition) {
        map[name] = definition
    }

    override fun unRegisterTaskDefinition(name: TaskIdentifier) {
        map.remove(name)
    }

    fun registerTaskDefinition(definition: TaskDefinition) {
        registerTaskDefinition(definition.identify, definition)
    }

    override fun getTaskDefinition(name: TaskIdentifier): TaskDefinition {
        return map[name] ?: throw TaskException("Task not define in system.")
    }

    fun getTaskDefinitions(): List<TaskDefinition> {
        return map.values.toList()
    }
}
