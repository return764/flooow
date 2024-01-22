package com.yutao.flooow.dsl

import com.yutao.flooow.enums.TaskType


interface TaskDSL {
    var name: String
    val type: TaskType
}

object task {
    operator fun invoke(init: Task.() -> Unit): Task {
        return Task().apply(init)
    }
}

fun runabc(block:() -> Unit) {
    block()
}
