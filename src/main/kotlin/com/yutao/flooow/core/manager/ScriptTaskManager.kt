package com.yutao.flooow.core.manager

import com.yutao.flooow.core.manager.registry.SimpleTaskDefinitionRegistry
import com.yutao.flooow.core.model.TaskDefinition
import com.yutao.flooow.core.coroutine.ApplicationRunnerCoroutineScope
import com.yutao.flooow.core.manager.scheduler.ComposeTaskSchedulers
import com.yutao.flooow.core.model.ExecutableTask
import com.yutao.flooow.dsl.DslParser
import com.yutao.flooow.utils.log
import com.yutao.flooow.watcher.FileChangeType
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component


@Component
class ScriptTaskManager(
    val composeTaskSchedulers: ComposeTaskSchedulers,
    val scope: ApplicationRunnerCoroutineScope,
    val parser: DslParser,
    val registry: SimpleTaskDefinitionRegistry
): InitializingBean {

    fun registerAndSchedule(definition: TaskDefinition) {
        log.info("Schedule task [${definition.identify.fileName}]")
        registry.registerTaskDefinition(definition)
        composeTaskSchedulers.schedule(build(definition))
    }

    fun registerAndUpdate(definition: TaskDefinition) {
        log.info("Update task [${definition.identify.fileName}]")
        registry.registerTaskDefinition(definition)
        composeTaskSchedulers.schedule(build(definition))
    }

    fun removeAndCancelTask(definition: TaskDefinition) {
        log.info("Remove task [${definition.identify.fileName}]")
        registry.unRegisterTaskDefinition(definition.identify)
        composeTaskSchedulers.cancel(build(definition))
    }

    fun build(taskDefinition: TaskDefinition): ExecutableTask {
        return ExecutableTask(
            type = taskDefinition.type,
            triggerManager = taskDefinition.triggerManager,
            taskChains = taskDefinition.taskChains,
            identify = taskDefinition.identify
        )
    }

    override fun afterPropertiesSet() {
        scope.launchAndCache {
            while (true) {
                val dsl = scope.taskDslChannel.receive()
                var definition: TaskDefinition? = null
                val parseResult = runCatching {
                    definition = parser.parse(dsl)
                }
                if (parseResult.isFailure) {
                    parseResult.onFailure { log.error("Parse task Error: ${it.message}") }
                    continue
                }

                if (dsl.fileChangeType == FileChangeType.CREATED) {
                    // 注册新的definition
                    // build task from definition
                    // 调度这个 task
                    registerAndSchedule(definition!!)
                } else if (dsl.fileChangeType == FileChangeType.MODIFIED) {
                    // 更新definition
                    // 重新build task from definition
                    // 移除之前的任务，重新创建新的任务
                    registerAndUpdate(definition!!)
                } else if (dsl.fileChangeType == FileChangeType.DELETED) {
                    // 移除definition
                    // 根据type和name移除任务
                    removeAndCancelTask(definition!!)
                }
            }
        }
    }
}
