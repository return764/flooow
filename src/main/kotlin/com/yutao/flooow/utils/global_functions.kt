package com.yutao.flooow.utils

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory


fun String.isTaskScriptFile(): Boolean {
    if (this.endsWith(".task.kts")) {
        return true
    }
    return false
}

fun String.getTaskScriptName(): String {
    require(this.isTaskScriptFile()) { "The script file name is invalid." }
    return this.dropLast(9)
}


inline val <reified T> T.log: Log
    get() = LogFactory.getLog(T::class.java)
