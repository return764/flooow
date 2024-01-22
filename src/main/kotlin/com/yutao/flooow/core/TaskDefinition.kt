package com.yutao.flooow.core

import com.yutao.flooow.enums.TaskType

class TaskDefinition(
    val executionTask: ExecutionTask
) {
    val type: TaskType = TaskType.MANUAL
    val name: String? = null
}

