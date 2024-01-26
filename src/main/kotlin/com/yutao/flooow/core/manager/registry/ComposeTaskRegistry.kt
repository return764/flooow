package com.yutao.flooow.core.manager.registry

import com.yutao.flooow.core.TaskDefinition
import org.springframework.stereotype.Component

@Component
class ComposeTaskRegistry(
    val registries: List<TaskRegistry>
) {

    fun register(definition: TaskDefinition) {
        registries
            .filter { it.accept(definition) }
            .forEach { it.register(definition) }
    }
}
