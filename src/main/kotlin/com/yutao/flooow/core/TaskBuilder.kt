package com.yutao.flooow.core

import com.yutao.flooow.dsl.Task

interface TaskBuilder {
    fun build(name: String): Task
}
