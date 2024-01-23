package com.yutao.flooow.run

import com.yutao.flooow.core.ExecutionTask

class DummyTask(
    val runner: () -> Unit
): ExecutionTask {
    override fun run() {
        runner()
    }

}
