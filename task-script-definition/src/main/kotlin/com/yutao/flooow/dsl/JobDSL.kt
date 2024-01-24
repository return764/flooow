package com.yutao.flooow.dsl

import java.util.LinkedList


class JobDSL(
    var parentTask: TaskDSL,
    override var name: String,
): RunnableDSL {
    var main: () -> Unit = {}
    var dependsOn: LinkedList<String> = LinkedList()

    fun dependsOn(name: String): JobDSL {
//        val savedJobNames = parentTask.jobs().map { it.name }
//        require(savedJobNames.contains(name) || parentTask.name == name) { "The dependent job/task must be defined." }

        dependsOn.add(name)
        return this
    }

    fun dependsOn(dsl: JobDSL): JobDSL {
        dependsOn(dsl.name)
        return this
    }

    fun dependsOn(vararg dsl: JobDSL): JobDSL {
        dsl.forEach { dependsOn(it) }
        return this
    }

    override fun main(block: () -> Unit) {
        main = block
    }
}
