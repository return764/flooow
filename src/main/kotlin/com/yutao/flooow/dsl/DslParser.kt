package com.yutao.flooow.dsl

import com.yutao.flooow.core.SubTask
import com.yutao.flooow.core.TaskChains
import com.yutao.flooow.core.TaskDefinition
import com.yutao.flooow.core.TriggerManager
import com.yutao.flooow.core.exception.TaskValidateException
import com.yutao.flooow.enums.TaskType
import org.springframework.stereotype.Component

@Component
class DslParser {
    fun parse(dls: MainTaskDSL): TaskDefinition {
        validate(dls)
        val dag = DirectedAcyclicGraph<String>()
        dag.addNode(dls.name)

        dls.jobs.forEach {
            dag.addNode(it.name)
            it.dependsOn.forEach { depend ->
                dag.addEdge(depend, it.name)
            }
        }

        if (!dag.emptyCheck()) {
            throw TaskValidateException("The graph of task definition can not be empty")
        }

        if (!dag.isolatedCheck()) {
            throw TaskValidateException("There are two or more unrelated task graphs")
        }

        if (!dag.circleCheck()) {
            throw TaskValidateException("There are circle in task graph, please check the job dependency")
        }

        val taskChains = TaskChains(mutableListOf(), dag)
        taskChains.tasks.add(SubTask(
            name = dls.name,
            runner = dls.main,
            chains = taskChains
        ))
        dls.jobs.forEach {
            SubTask(
                name = it.name,
                runner = it.main,
                chains = taskChains
            ).also { taskChains.tasks.add(it) }
        }

        // TODO support more type
        val type = if (dls.triggerDSL.cron == null) TaskType.MANUAL else TaskType.TIMER

        return TaskDefinition(
            clazz = dls::class,
            name = dls.name,
            type = type,
            taskChains = taskChains,
            triggerManager = TriggerManager(dls.triggerDSL)
        )
    }

    fun validate(dls: MainTaskDSL) {
        val allJobs = dls.jobs.map { it.name }.toMutableList()
            .also { it.add(dls.name) }
        val jobNameDependsSet = HashSet<String>()
        dls.jobs
            .map { it.dependsOn }
            .forEach {
                val nextSet = it.toHashSet()
                if (nextSet.size < it.size) {
                    throw TaskValidateException("The job depends declaration was duplicate, please check the script file.")
                }
                jobNameDependsSet.addAll(nextSet)
            }

        jobNameDependsSet.forEach {
            if (!allJobs.contains(it)) {
                throw TaskValidateException("The depends job [$it] was not found.")
            }
        }
    }
}
