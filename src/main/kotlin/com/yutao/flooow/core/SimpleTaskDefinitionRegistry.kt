package com.yutao.flooow.core

import com.yutao.flooow.core.exception.TaskException

open class SimpleTaskDefinitionRegistry: TaskDefinitionRegistry {
    private val map = mutableMapOf<String, TaskDefinition>()

    override fun registerTaskDefinition(name: String, definition: TaskDefinition) {
        // TODO
//        if (map.containsKey(name)) {
//            throw TaskException("The system has already register current Task, please change your task name to avoid conflict.")
//        }
        map[name] = definition
    }

    fun registerTaskDefinition(definition: TaskDefinition) {
        registerTaskDefinition(definition.name, definition)
    }

    override fun getTaskDefinition(name: String): TaskDefinition {
        return map[name] ?: throw TaskException("Task not define in system.")
    }

    fun getTaskDefinitions(): List<TaskDefinition> {
        return map.values.toList()
    }
}
