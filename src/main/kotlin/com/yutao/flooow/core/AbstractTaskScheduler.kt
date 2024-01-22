package com.yutao.flooow.core

abstract class AbstractTaskScheduler: AbstractTaskBuilder(), TaskScheduler, TaskDefinitionRegistry {
    private val executionTaskQueue = mutableListOf<ExecutionTask>()

    override fun schedule() {
        // 添加所有的任务到队列中
        executionTaskQueue.addAll(getTaskDefinitions().map { it.executionTask })

        for (task in executionTaskQueue) {
            task.run()
        }

        // 销毁
        executionTaskQueue.clear()
    }

    override fun pushTask2Queue(executionTask: ExecutionTask) {
        executionTaskQueue.add(executionTask)
    }
}
