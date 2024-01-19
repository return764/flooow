package com.yutao.flooow.core

import com.yutao.flooow.dsl.Task


abstract class AbstractTaskBuilder: SimpleTaskDefinitionRegistry(), TaskBuilder {
    override fun build(name: String): Task {
        // 读取definition
        val taskDefinition = getTaskDefinition(name)
        // 构建任务
        val task = taskDefinition.task
        // 添加任务到队列中
        pushTask2Queue(task)
        return task
    }

    protected abstract fun pushTask2Queue(task: Task)

}
