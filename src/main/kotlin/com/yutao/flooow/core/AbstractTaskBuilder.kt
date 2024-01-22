package com.yutao.flooow.core


abstract class AbstractTaskBuilder: SimpleTaskDefinitionRegistry(), TaskBuilder {
    override fun build(name: String): ExecutionTask {
        // 读取definition
        val taskDefinition = getTaskDefinition(name)
        // 构建任务
        val task = taskDefinition.executionTask
        // 添加任务到队列中
        pushTask2Queue(task)
        return task
    }

    protected abstract fun pushTask2Queue(executionTask: ExecutionTask)

}
