package com.yutao.flooow.core

import com.yutao.flooow.dsl.Task

class TaskScheduler {
    val taskQueue = mutableListOf<Task>()

    fun registerTask(taskDefinition: TaskDefinition) {
        taskQueue.add(taskDefinition.task)
    }

    fun schedule() {
        for (task in taskQueue) {
            task.run()
        }
    }
}
