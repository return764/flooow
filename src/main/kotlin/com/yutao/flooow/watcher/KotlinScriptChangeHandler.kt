package com.yutao.flooow.watcher

import com.yutao.flooow.core.coroutine.ApplicationRunnerCoroutineScope
import com.yutao.flooow.dsl.MainTaskDSL
import com.yutao.flooow.host.TaskScriptHost
import com.yutao.flooow.utils.getTaskScriptName
import com.yutao.flooow.utils.isTaskScriptFile
import org.springframework.stereotype.Component
import java.io.File
import kotlin.script.experimental.api.asSuccess
import kotlin.script.experimental.api.onFailure
import kotlin.script.experimental.api.onSuccess

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
        // compilation exception
        result.onFailure {
            println(it.reports)
        }
        // not error in compilation but evaluation could exist
        result.onSuccess {
            it.asSuccess()
        }
        // handle result
        scope.taskDslChannel.send(dsl)
    }

    override fun acceptProcess(file: File): Boolean {
        return file.name.isTaskScriptFile()
    }

}
