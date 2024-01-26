package com.yutao.flooow.watcher

import java.io.File

interface FileChangeHandler {
    suspend fun handle(file: File, fileChangeType: FileChangeType)

    fun acceptProcess(file: File): Boolean
}
