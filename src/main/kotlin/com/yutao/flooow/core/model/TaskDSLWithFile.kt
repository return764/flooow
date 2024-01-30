package com.yutao.flooow.core.model

import com.yutao.flooow.dsl.MainTaskDSL
import com.yutao.flooow.watcher.FileChangeType
import java.io.File

class TaskDSLWithFile(
    val mainTaskDSL: MainTaskDSL,
    val file: File,
    val fileChangeType: FileChangeType
) {
}
