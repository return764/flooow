package com.yutao.flooow.core

import com.yutao.flooow.core.exception.TaskException

open class SimpleTaskDefinitionRegistry: TaskDefinitionRegistry {
    private val map = mutableMapOf<String, TaskDefinition>()

    override fun registerTaskDefinition(name: String, definition: TaskDefinition) {
        map[name] = definition
    }

    override fun getTaskDefinition(name: String): TaskDefinition {
        return map[name] ?: throw TaskException("Task not define in system.")
    }

    fun getTaskDefinitions(): List<TaskDefinition> {
        return map.values.toList()
    }
}
