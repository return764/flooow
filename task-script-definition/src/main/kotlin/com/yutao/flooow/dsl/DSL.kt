package com.yutao.flooow.dsl

interface RunnableDSL {
    var name: String

    fun main(block: () -> Unit)
}

interface TaskDSL : RunnableDSL {
    fun jobs(name: String): JobDSL
    fun jobs(): List<JobDSL>
    fun triggerManager(): TriggerDSL
    fun createJob(jobDSL: JobDSL)
}

fun TaskDSL.job(name: String, configuration: JobDSL.() -> Unit): JobDSL {
    val job = JobDSL(
        parentTask = this,
        name = name
    ).apply(configuration)
    createJob(job)
    return job
}

fun TaskDSL.trigger(configuration: TriggerDSL.() -> Unit): TaskDSL {
    triggerManager().apply(configuration)
    return this
}

infix fun JobDSL.depend(dsl: JobDSL): JobDSL {
    this.dependsOn(dsl.name)
    return dsl
}
