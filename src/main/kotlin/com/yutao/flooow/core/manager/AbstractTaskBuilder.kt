package com.yutao.flooow.core.manager

import com.yutao.flooow.core.TaskDefinition


abstract class AbstractTaskBuilder : TaskBuilder {
    override fun build(taskDefinition: TaskDefinition): ExecutableTask {
        // 读取definition

        val executableTask = ExecutableTask(
            type = taskDefinition.type,
            triggerManager = taskDefinition.triggerManager,
            taskChains = taskDefinition.taskChains,
            name = taskDefinition.name
        )
        // 构建任务
        return executableTask
    }


}
