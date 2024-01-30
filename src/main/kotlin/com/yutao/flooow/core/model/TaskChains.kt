package com.yutao.flooow.core.model

import com.yutao.flooow.dsl.DirectedAcyclicGraph

class TaskChains(
    val tasks: MutableList<SubTask>,
    val dag: DirectedAcyclicGraph<String>
) {

    fun getMainTask(): SubTask {
        return tasks.find { it.name == dag.getFirst()!! }!!
    }

    fun getTaskByName(name: String): SubTask {
        return tasks.find { it.name == name }!!
    }

    fun waitingExecute(): List<SubTask> {
        return tasks.filter { it.status == TaskRuntimeState.PENDING }
    }
}
