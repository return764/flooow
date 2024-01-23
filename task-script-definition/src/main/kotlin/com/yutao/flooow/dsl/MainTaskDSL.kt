package com.yutao.flooow.dsl


class MainTaskDSL(
    override var name: String,
) : TaskDSL {
    var main: () -> Unit = {}
    var jobs: MutableList<JobDSL> = mutableListOf()
    override fun jobs(name: String): JobDSL {
        val job = jobs.find { it.name == name }
        require(job != null)
        return job
    }

    override fun createJob(jobDSL: JobDSL) {
        jobs.add(jobDSL)
    }

    override fun main(block: () -> Unit) {
        main = block
    }

}
