package com.yutao.flooow.core

interface TaskDefinitionRegistry {
    fun registerTaskDefinition(name: String, definition: TaskDefinition)
    fun unRegisterTaskDefinition(name: String)
    fun getTaskDefinition(name: String): TaskDefinition
}
