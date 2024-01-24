package com.yutao.flooow.dsl

import com.yutao.flooow.core.TaskDefinition
import com.yutao.flooow.core.exception.TaskValidateException
import com.yutao.flooow.enums.TaskType
import org.springframework.stereotype.Component

@Component
class DslParser {
    fun parse(dls: MainTaskDSL): TaskDefinition {
        validate(dls)
        val dag = DirectedAcyclicGraph<String>()

        val subTasks = dls.jobs.map {
            it.dependsOn.forEach { depend ->
                dag.addEdge(depend, it.name)
            }
            TaskDefinition.SubTask(
                name = it.name,
                runner = it.main
            )
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


        return TaskDefinition(
            clazz = dls::class,
            name = dls.name,
            type = TaskType.MANUAL,
            runner = dls.main,
            subTasks = subTasks,
            dag = dag
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
