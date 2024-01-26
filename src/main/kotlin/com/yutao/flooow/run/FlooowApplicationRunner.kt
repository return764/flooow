package com.yutao.flooow.run

import com.yutao.flooow.core.coroutine.ApplicationRunnerCoroutineScope
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class FlooowApplicationRunner(
    val scope: ApplicationRunnerCoroutineScope,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) = runBlocking(scope.coroutineContext) {
        // 1. start watching folder
        // 2. response for file change
        // 3. compile and evaluate new script

        // 4. build TaskDefinition from TaskDSL
        // 5. run task in the time by scheduler
        // 6. collect task result
        scope.joinAll()
    }
}
