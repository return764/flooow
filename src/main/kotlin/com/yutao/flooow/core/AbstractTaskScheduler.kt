package com.yutao.flooow.core

import com.yutao.flooow.dsl.Task

abstract class AbstractTaskScheduler: AbstractTaskBuilder(), TaskScheduler, TaskDefinitionRegistry {
    private val taskQueue = mutableListOf<Task>()

    override fun schedule() {
        // 添加所有的任务到队列中
        taskQueue.addAll(getTaskDefinitions().map { it.task })

        for (task in taskQueue) {
            task.run()
        }

        // 销毁
        taskQueue.clear()
    }

    override fun pushTask2Queue(task: Task) {
        taskQueue.add(task)
    }
}
