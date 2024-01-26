package com.yutao.flooow.core.scheduler.registry

import com.yutao.flooow.core.TaskDefinition
import com.yutao.flooow.enums.TaskType
import org.springframework.stereotype.Component

@Component
class TimerTaskRegistry: TaskRegistry {
    override fun register(definition: TaskDefinition) {
        TODO("Not yet implemented")
    }

    override fun accept(definition: TaskDefinition): Boolean {
        return definition.type == TaskType.TIMER
    }
}
