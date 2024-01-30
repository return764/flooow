package com.yutao.flooow.core.model

import com.yutao.flooow.enums.TaskType

class ExecutableTask(
    val identify: TaskIdentifier,
    val type: TaskType,
    val triggerManager: TriggerManager,
    val taskChains: TaskChains
) : Runnable, Repeatable {
    override fun run() {
        val mainTask = taskChains.getMainTask()
        mainTask.pending()

        if (mainTask.couldExecute()) {
            mainTask.run()
        }

        while (true) {
            if (taskChains.done()) {
                break
            }
            val pendingTasks = taskChains.waitingExecute()
            pendingTasks.forEach { task ->
                if (task.couldExecute()) {
                    task.run()
                }
            }
        }
        reset()
    }

    private fun TaskChains.done(): Boolean {
        return this.tasks.all { it.status == TaskRuntimeState.FULFILLED } || this.tasks.any { it.status == TaskRuntimeState.REJECTED }
    }

    private fun SubTask.couldExecute(): Boolean {
        return this.status == TaskRuntimeState.PENDING
                && this.getPrevious().isEmpty()
                || this.getPrevious().all { it.status == TaskRuntimeState.FULFILLED }
    }

    override fun reset() {
        taskChains.tasks.forEach { it.reset() }
    }

}
