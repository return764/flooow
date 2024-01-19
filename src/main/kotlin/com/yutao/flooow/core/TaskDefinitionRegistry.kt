package com.yutao.flooow.core

interface TaskDefinitionRegistry {
    fun registerTaskDefinition(name: String, definition: TaskDefinition)
    fun getTaskDefinition(name: String): TaskDefinition
}
