package com.yutao.flooow.core

import com.yutao.flooow.core.coroutine.ApplicationRunnerCoroutineScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.InitializingBean
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component


@Component
class DefaultScriptTaskScheduler(
    val scope: ApplicationRunnerCoroutineScope,
    val taskScheduler: ThreadPoolTaskScheduler
) : ScriptTaskScheduler, InitializingBean, AbstractScriptTaskScheduler() {

    override fun afterPropertiesSet() {
        scope.launch {
            schedule()
        }
    }

}
