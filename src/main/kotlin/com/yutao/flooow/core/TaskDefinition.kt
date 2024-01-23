package com.yutao.flooow.core

import com.yutao.flooow.enums.TaskType

class TaskDefinition(
    val type: TaskType,
    val name: String,
    val runner: () -> Unit
) {

}

