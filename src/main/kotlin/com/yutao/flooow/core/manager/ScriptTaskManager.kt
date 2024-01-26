package com.yutao.flooow.core.manager

import com.yutao.flooow.core.SimpleTaskDefinitionRegistry
import com.yutao.flooow.core.TaskDefinition
import com.yutao.flooow.core.coroutine.ApplicationRunnerCoroutineScope
import com.yutao.flooow.core.exception.TaskException
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
    val scope: ApplicationRunnerCoroutineScope,
    val parser: DslParser,
    val registry: SimpleTaskDefinitionRegistry
): AbstractTaskBuilder(), InitializingBean {

    fun registerAndSchedule(definition: TaskDefinition) {
        registry.registerTaskDefinition(definition)
        schedule(build(definition))
    }

    fun removeAndCancelTask(definition: TaskDefinition) {
        registry.unRegisterTaskDefinition(definition.name)
        cancel(build(definition))
    }

    fun cancel(task: ExecutableTask) {
        if (task.type === TaskType.TIMER) {
            timerTaskScheduler.cancel(task)
        }
    }

    fun schedule(task: ExecutableTask) {
        if (task.type === TaskType.TIMER) {
            timerTaskScheduler.cancel(task)
            timerTaskScheduler.schedule(task)
        }
    }

    override fun afterPropertiesSet() {
        scope.launchAndCache {
            while (true) {
                val dsl = scope.taskDslChannel.receive()
                var definition: TaskDefinition? = null
                val parseResult = runCatching {
                    definition = parser.parse(dsl.mainTaskDSL)
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
                    registerAndSchedule(definition!!)
                } else if (dsl.fileChangeType == FileChangeType.DELETED) {
                    // 移除definition
                    // 根据type和name移除任务
                    removeAndCancelTask(definition!!)
                }
            }
        }
    }
}
