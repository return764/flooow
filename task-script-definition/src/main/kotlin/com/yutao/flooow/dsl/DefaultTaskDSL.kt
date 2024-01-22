package com.yutao.flooow.dsl

import com.yutao.flooow.enums.TaskType

class DefaultTaskDSL(
    override var name: String,
    override val type: TaskType
) : TaskDSL {

}
