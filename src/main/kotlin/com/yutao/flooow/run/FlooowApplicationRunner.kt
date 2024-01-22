package com.yutao.flooow.run

import com.yutao.flooow.core.DefaultTaskScheduler
import com.yutao.flooow.core.TaskDefinition
import com.yutao.flooow.dsl.DefaultTaskDSL
import com.yutao.flooow.enums.TaskType
import com.yutao.flooow.host.TaskScriptHost
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.nio.file.Paths


@Component
class FlooowApplicationRunner : ApplicationRunner {
    override fun run(args: ApplicationArguments?) = runBlocking {
//        val watcher = LocalDirectoryWatcher()
//        launch { watcher.watch() }

        val scriptFile = Paths.get(System.getProperty("user.dir"), "dags", "test.task.kts").toFile()

        val dsl = DefaultTaskDSL(
            "default name",
            TaskType.MANUAL
        )
        val scriptHost = TaskScriptHost()
        val eval = scriptHost.eval(scriptFile, dsl)
        println(dsl.name)

        val task = TestTask()
        val taskDefinition = TaskDefinition(task)
        val scheduler = DefaultTaskScheduler()
        scheduler.registerTaskDefinition("testTask", taskDefinition)
        scheduler.schedule()
    }
}
