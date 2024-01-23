package com.yutao.flooow.watcher

import com.yutao.flooow.core.coroutine.ApplicationRunnerCoroutineScope
import com.yutao.flooow.core.exception.TaskException
import com.yutao.flooow.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import kotlin.io.path.pathString

@Component
class LocalDirectoryWatcher(
    val fileChangeHandler: AbstractFileChangeHandler,
    val applicationRunnerCoroutineScope: ApplicationRunnerCoroutineScope
) : Watcher, InitializingBean {
    private val location: String = System.getProperty("user.dir")
    private val dirName = "dags"

    override suspend fun startWatch() {
        try {
            checkDirectoryIfExists()
        } catch (e: SecurityException) {
            throw TaskException("can't create dags folder, because of some security reason.")
        }

        val watchService = withContext(Dispatchers.IO) {
            FileSystems.getDefault().newWatchService()
        }
        val directory = Paths.get(location, dirName)

        directory.toFile().listFiles().forEach { fileChangeHandler.onHandle(it) }

        withContext(Dispatchers.IO) {
            directory.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY
            )
        }
        log.info("Started watching scripts directory：${directory.pathString}")
        applicationRunnerCoroutineScope.launchAndCache {
            while (true) {
                val key = withContext(Dispatchers.IO) {
                    watchService.take()
                } // 获取下一个文件事件
                for (event in key.pollEvents()) {
                    val kind = event.kind()

                    // 处理文件事件
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        val createdFile = directory.resolve(event.context() as Path)
                        println("创建文件：$createdFile")
                        fileChangeHandler.onHandle(createdFile.toFile())
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        val modifiedFile = directory.resolve(event.context() as Path)
                        println("修改文件：$modifiedFile")
                        fileChangeHandler.onHandle(modifiedFile.toFile())
                    }
                }
                key.reset() // 重置监听器
            }
        }
    }

    private fun checkDirectoryIfExists() {
        val directoryPath = Paths.get(location, dirName)

        if (Files.notExists(directoryPath)) {
            Files.createDirectory(directoryPath)
        }
    }

    override fun afterPropertiesSet() {
        applicationRunnerCoroutineScope.launch {
            startWatch()
        }
    }
}
