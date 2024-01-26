package com.yutao.flooow.core.manager.registry

import com.yutao.flooow.core.TaskDefinition
import com.yutao.flooow.enums.TaskType
import org.springframework.stereotype.Component

@Component
class ManualTaskRegistry: TaskRegistry {
    override fun register(definition: TaskDefinition) {
        TODO("Not yet implemented")
    }

    override fun accept(definition: TaskDefinition): Boolean {
        return definition.type === TaskType.MANUAL
    }
}
