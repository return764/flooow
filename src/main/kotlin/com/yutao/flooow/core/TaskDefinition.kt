package com.yutao.flooow.core

import com.yutao.flooow.dsl.DirectedAcyclicGraph
import com.yutao.flooow.dsl.TriggerDSL
import com.yutao.flooow.enums.TaskType
import com.yutao.flooow.utils.log
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.CronTrigger
import kotlin.reflect.KClass

class TaskDefinition(
    val clazz: KClass<*>,
    val type: TaskType,
    val name: String,
    val taskChains: TaskChains,
    val triggerManager: TriggerManager
) {


}


class SubTask(
    val name: String,
    val runner: () -> Unit,
    var status: TaskRuntimeState = TaskRuntimeState.CREATED,
    val chains: TaskChains
): Repeatable {
    fun List<SubTask>.pending() = this.forEach { it.pending() }

    fun run() {
        this.running()
        val result = runCatching {
            runner()
            this.fulfilled()
            this.getNext().pending()

        }
        result.onFailure {
            log.error("Run ${this.name} failed. with error ${it.message}")
            this.rejected()
        }
    }
    fun getNext(): List<SubTask> {
        return chains.dag.next(this.name).orElse(listOf()).map { chains.getTaskByName(it) }
    }

    fun getPrevious(): List<SubTask> {
        return chains.dag.previous(this.name).orElse(listOf()).map { chains.getTaskByName(it) }
    }

    fun pending() {
        require(this.status == TaskRuntimeState.CREATED || this.status == TaskRuntimeState.PENDING)
        this.status = TaskRuntimeState.PENDING
    }

    fun running() {
        require(this.status == TaskRuntimeState.PENDING)
        this.status = TaskRuntimeState.RUNNING
    }

    fun fulfilled() {
        require(this.status == TaskRuntimeState.RUNNING)
        this.status = TaskRuntimeState.FULFILLED
    }

    fun rejected() {
        this.status = TaskRuntimeState.REJECTED
    }

    override fun reset() {
        this.status = TaskRuntimeState.CREATED
    }
}

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

class TriggerManager(
    val triggerDSL: TriggerDSL
) {
    fun getTrigger(): Trigger {
        // TODO support more trigger
        return CronTrigger(triggerDSL.cron!!)
    }
}
