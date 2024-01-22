package com.yutao.flooow.core

interface TaskBuilder {
    fun build(name: String): ExecutionTask
}
