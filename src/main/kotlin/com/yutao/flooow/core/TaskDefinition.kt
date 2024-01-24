package com.yutao.flooow.core

import com.yutao.flooow.dsl.DirectedAcyclicGraph
import com.yutao.flooow.enums.TaskType
import kotlin.reflect.KClass

class TaskDefinition(
    val clazz: KClass<*>,
    val type: TaskType,
    val name: String,
    val runner: () -> Unit,
    val subTasks: List<SubTask>,
    val dag: DirectedAcyclicGraph<String>

) {
    class SubTask(
        val name: String,
        val runner: () -> Unit
    )
}

