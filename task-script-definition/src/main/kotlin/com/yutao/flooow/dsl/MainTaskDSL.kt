package com.yutao.flooow.dsl


class MainTaskDSL(
    override var name: String,
) : TaskDSL {
    var main: () -> Unit = {}
    var jobs: MutableList<JobDSL> = mutableListOf()
    var triggerDSL = TriggerDSL()

    override fun jobs(name: String): JobDSL {
        val job = jobs.find { it.name == name }
        require(job != null)
        return job
    }

    override fun jobs(): List<JobDSL> {
        return jobs
    }

    override fun triggerManager(): TriggerDSL {
        return triggerDSL
    }

    override fun createJob(jobDSL: JobDSL) {
        jobs.add(jobDSL)
    }

    override fun main(block: () -> Unit) {
        main = block
    }

}
