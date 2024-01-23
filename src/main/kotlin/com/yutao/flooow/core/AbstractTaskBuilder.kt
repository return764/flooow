package com.yutao.flooow.core

import com.yutao.flooow.run.DummyTask


abstract class AbstractTaskBuilder: SimpleTaskDefinitionRegistry(), TaskBuilder {
    override fun build(name: String): ExecutionTask {
        // 读取definition
        val taskDefinition = getTaskDefinition(name)
        // 构建任务
        val task = DummyTask(taskDefinition.runner)
        // 添加任务到队列中
        pushTask2Queue(task)
        return task
    }

    protected abstract fun pushTask2Queue(executionTask: ExecutionTask)

}
