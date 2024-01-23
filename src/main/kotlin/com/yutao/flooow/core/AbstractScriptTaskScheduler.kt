package com.yutao.flooow.core

abstract class AbstractScriptTaskScheduler : AbstractTaskBuilder(), ScriptTaskScheduler, TaskDefinitionRegistry {
    private val executionTaskQueue = mutableListOf<ExecutionTask>()

    override fun schedule() {
        // TODO 重写调度逻辑
        executionTaskQueue.addAll(getTaskDefinitions().map { build(it.name) })

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
