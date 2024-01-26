package com.yutao.flooow.core.scheduler


interface TaskBuilder {
    fun build(name: String): ExecutableTask
}
