package com.yutao.flooow.core.model

import com.yutao.flooow.enums.TaskType
import kotlin.reflect.KClass

class TaskDefinition(
    val clazz: KClass<*>,
    val type: TaskType,
    val identify: TaskIdentifier,
    val taskChains: TaskChains,
    val triggerManager: TriggerManager
)


