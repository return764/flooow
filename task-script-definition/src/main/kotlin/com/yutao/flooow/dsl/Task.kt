package com.yutao.flooow.dsl

import com.yutao.flooow.enums.TaskType

class Task() {
    lateinit var name: String
    var type: TaskType = TaskType.MANUAL
    lateinit var taskContent: () -> Unit

    fun run(block: () -> Unit) {
        this.taskContent = block
    }
}
