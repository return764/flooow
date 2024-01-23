package com.yutao.flooow.dsl


class JobDSL(
    override var name: String,
): RunnableDSL {
    var main: () -> Unit = {}
    lateinit var dependsOn: String

    fun dependsOn(name: String) {
        dependsOn = name
    }

    fun dependsOn(dsl: TaskDSL) {
        dependsOn = dsl.name
    }

    override fun main(block: () -> Unit) {
        main = block
    }
}
