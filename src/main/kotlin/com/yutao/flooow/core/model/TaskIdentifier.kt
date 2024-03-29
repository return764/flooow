package com.yutao.flooow.core.model

class TaskIdentifier(
    val name: String,
    val fileName: String
) {
    fun identify() = "$name/$fileName"

    override fun toString(): String {
        return identify()
    }
}
