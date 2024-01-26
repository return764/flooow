package com.yutao.flooow.core.coroutine

import com.yutao.flooow.dsl.MainTaskDSL
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Component
class ApplicationRunnerCoroutineScope(val scope: CoroutineScope = CoroutineScope(CoroutineName("SpringRunner"))) :
    CoroutineScope by scope {

    val jobs: MutableList<Job> = mutableListOf()
    val taskDslChannel: Channel<TaskDSLWithFile> = Channel()

    fun launchAndCache(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        val job = launch(context, start, block)
        jobs.add(job)
        return job
    }


    suspend fun joinAll() {
        jobs.forEach { it.join() }
    }
}
