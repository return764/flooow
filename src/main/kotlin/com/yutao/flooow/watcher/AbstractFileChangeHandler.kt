package com.yutao.flooow.watcher

import java.io.File

abstract class AbstractFileChangeHandler: FileChangeHandler {
    suspend fun onHandle(file: File) {
        if (needProcess(file)) {
            handle(file)
        }
    }
}
