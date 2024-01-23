package com.yutao.flooow.dsl

import com.yutao.flooow.core.TaskDefinition
import com.yutao.flooow.enums.TaskType
import org.springframework.stereotype.Component

@Component
class DslParser {
    fun parse(dls: MainTaskDSL): TaskDefinition {

        return TaskDefinition(
            name = dls.name,
            type = TaskType.MANUAL,
            runner = dls.main
        )
    }
}
