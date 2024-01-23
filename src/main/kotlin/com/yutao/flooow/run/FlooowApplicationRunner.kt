package com.yutao.flooow.run

import com.yutao.flooow.core.DefaultScriptTaskScheduler
import com.yutao.flooow.core.coroutine.ApplicationRunnerCoroutineScope
import com.yutao.flooow.dsl.DslParser
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class FlooowApplicationRunner(
    val scope: ApplicationRunnerCoroutineScope,
    val parser: DslParser,
    val scheduler: DefaultScriptTaskScheduler
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) = runBlocking(scope.coroutineContext) {
        // 1. start watching folder
        // 2. response for file change
        // 3. compile and evaluate new script

        // 4. build TaskDefinition from TaskDSL
        launch {
            while (true) {
                val dsl = scope.taskDslChannel.receive()
                println("start parse DSL")
                val definition = parser.parse(dsl)
                scheduler.registerTaskDefinition(definition)
            }
        }
        // 5. run task in the time by scheduler
        scheduler.schedule()

        // 6. collect task result


        scope.joinAll()
    }
}
