package com.yutao.flooow.core

interface TaskDefinitionRegistry {
    fun registerTaskDefinition(name: TaskIdentifier, definition: TaskDefinition)
    fun unRegisterTaskDefinition(name: TaskIdentifier)
    fun getTaskDefinition(name: TaskIdentifier): TaskDefinition
}
