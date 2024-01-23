package com.yutao.flooow.watcher

import java.io.File

interface FileChangeHandler {
    suspend fun handle(file: File)

    fun needProcess(file: File): Boolean
}
