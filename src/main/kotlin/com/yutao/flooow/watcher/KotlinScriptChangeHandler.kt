package com.yutao.flooow.watcher

import com.yutao.flooow.core.coroutine.ApplicationRunnerCoroutineScope
import com.yutao.flooow.dsl.MainTaskDSL
import com.yutao.flooow.host.TaskScriptHost
import com.yutao.flooow.utils.getTaskScriptName
import com.yutao.flooow.utils.isTaskScriptFile
import com.yutao.flooow.utils.log
import org.springframework.stereotype.Component
import java.io.File
import kotlin.script.experimental.api.onFailure
import kotlin.script.experimental.api.valueOrNull

@Component
class KotlinScriptChangeHandler(
    val scope: ApplicationRunnerCoroutineScope
) : AbstractFileChangeHandler(), FileChangeHandler {
    val host: TaskScriptHost = TaskScriptHost()
    override suspend fun handle(file: File) {
        val dsl = MainTaskDSL(
            name = file.name.getTaskScriptName()
        )
        // compiling
        // evaluation
        val result = host.eval(file, dsl)
        result.onFailure {
            println(it.reports)
        }
        // handle result
        scope.taskDslChannel.send(dsl)
    }

    override fun needProcess(file: File): Boolean {
        return file.name.isTaskScriptFile()
    }

}
