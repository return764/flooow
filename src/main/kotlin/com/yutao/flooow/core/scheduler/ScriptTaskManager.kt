package com.yutao.flooow.core.scheduler

import com.yutao.flooow.core.TaskDefinition
import com.yutao.flooow.core.coroutine.ApplicationRunnerCoroutineScope
import com.yutao.flooow.dsl.DslParser
import com.yutao.flooow.enums.TaskType
import com.yutao.flooow.utils.log
import kotlinx.coroutines.launch
import org.springframework.beans.factory.InitializingBean
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component


@Component
class ScriptTaskManager(
    val taskScheduler: ThreadPoolTaskScheduler,
    val scope: ApplicationRunnerCoroutineScope,
    val parser: DslParser,
): AbstractTaskBuilder(), InitializingBean {

    private val cachedTaskDefinition = mutableListOf<TaskDefinition>()

    override fun registerTaskDefinition(name: String, definition: TaskDefinition) {
        super.registerTaskDefinition(name, definition)
        // add or update
        val cache = cachedTaskDefinition.find { it.name == name }
        if (cache != null) {
            cachedTaskDefinition.remove(cache)
        }
        cachedTaskDefinition.add(definition)
        schedule(build(definition.name))
    }

    fun schedule(task: ExecutableTask) {
        if (task.type === TaskType.TIMER) {
            taskScheduler.schedule(task, task.triggerManager.getTrigger())
        }
    }

    override fun afterPropertiesSet() {
        scope.launchAndCache {
            while (true) {
                val dsl = scope.taskDslChannel.receive()
                log.info("Start parse task DSL")
                val definition = parser.parse(dsl)
                registerTaskDefinition(definition)
            }
        }
    }
}
