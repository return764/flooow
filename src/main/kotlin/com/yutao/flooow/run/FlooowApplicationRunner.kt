package com.yutao.flooow.run

import com.yutao.flooow.core.TaskDefinition
import com.yutao.flooow.core.DefaultTaskScheduler
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class FlooowApplicationRunner : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val task = TestTask()
        val taskDefinition = TaskDefinition(task)
        val scheduler = DefaultTaskScheduler()
        scheduler.registerTaskDefinition("testTask", taskDefinition)
        scheduler.schedule()
    }
}
