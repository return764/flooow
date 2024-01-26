package com.yutao.flooow.core.scheduler

import com.yutao.flooow.core.SimpleTaskDefinitionRegistry


abstract class AbstractTaskBuilder: SimpleTaskDefinitionRegistry(), TaskBuilder {
    override fun build(name: String): ExecutableTask {
        // 读取definition
        val taskDefinition = getTaskDefinition(name)

        val executableTask = ExecutableTask(
            type = taskDefinition.type,
            triggerManager = taskDefinition.triggerManager,
            taskChains = taskDefinition.taskChains
        )
        // 构建任务
        return executableTask
    }


}
