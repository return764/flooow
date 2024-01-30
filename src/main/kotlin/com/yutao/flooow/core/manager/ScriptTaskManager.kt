package com.yutao.flooow.core.manager

import com.yutao.flooow.core.SimpleTaskDefinitionRegistry
import com.yutao.flooow.core.TaskDefinition
import com.yutao.flooow.core.coroutine.ApplicationRunnerCoroutineScope
import com.yutao.flooow.core.exception.TaskException
import com.yutao.flooow.core.manager.scheduler.ApiTaskScheduler
import com.yutao.flooow.core.manager.scheduler.TimerTaskScheduler
import com.yutao.flooow.dsl.DslParser
import com.yutao.flooow.enums.TaskType
import com.yutao.flooow.utils.log
import com.yutao.flooow.watcher.FileChangeType
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component


@Component
class ScriptTaskManager(
    val timerTaskScheduler: TimerTaskScheduler,
    val apiTaskScheduler: ApiTaskScheduler,
    val scope: ApplicationRunnerCoroutineScope,
    val parser: DslParser,
    val registry: SimpleTaskDefinitionRegistry
): AbstractTaskBuilder(), InitializingBean {

    fun registerAndSchedule(definition: TaskDefinition) {
        log.info("Schedule task [${definition.identify.fileName}]")
        registry.registerTaskDefinition(definition)
        schedule(build(definition))
    }

    fun registerAndUpdate(definition: TaskDefinition) {
        log.info("Update task [${definition.identify.fileName}]")
        registry.registerTaskDefinition(definition)
        schedule(build(definition))
    }

    fun removeAndCancelTask(definition: TaskDefinition) {
        log.info("Remove task [${definition.identify.fileName}]")
        registry.unRegisterTaskDefinition(definition.identify)
        cancel(build(definition))
    }

    fun cancel(task: ExecutableTask) {
        if (task.type === TaskType.TIMER) {
            timerTaskScheduler.cancel(task)
        }else if (task.type === TaskType.API) {
            apiTaskScheduler.cancel(task)
        }
    }



    fun schedule(task: ExecutableTask) {
        if (task.type === TaskType.TIMER) {
            timerTaskScheduler.schedule(task)
        } else if (task.type === TaskType.API) {
            apiTaskScheduler.schedule(task)
        }
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
