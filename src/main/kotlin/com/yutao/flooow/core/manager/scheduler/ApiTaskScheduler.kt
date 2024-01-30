package com.yutao.flooow.core.manager.scheduler

import com.yutao.flooow.core.exception.TaskException
import com.yutao.flooow.core.model.ExecutableTask
import com.yutao.flooow.enums.TaskType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

@Component
class ApiTaskScheduler(
    val apiRegister: RequestMappingHandlerMapping
): TaskScheduler {
    private val prefix = "/graph"
    val requestMappings = mutableMapOf<String, RequestMappingInfo>()

    override fun cancel(task: ExecutableTask) {
        requestMappings[task.identify.fileName]?.let { apiRegister.unregisterMapping(it) }
    }

    override fun support(): TaskType {
        return TaskType.API
    }

    override fun schedule(task: ExecutableTask) {
        val requestMappingInfo = RequestMappingInfo
            .paths("$prefix/${task.identify.name}")
            .methods(RequestMethod.POST)
            .build()
        requestMappings[task.identify.fileName] = requestMappingInfo
        val wrapped = ApiWrapper(task)
        apiRegister.registerMapping(
            requestMappingInfo,
            wrapped,
            ApiWrapper::class.java.getDeclaredMethod(
                "run",
                HttpServletRequest::class.java,
                HttpServletResponse::class.java
            )
        )
    }

    class ApiWrapper(
        val task: ExecutableTask
    ) {

        fun run(request: HttpServletRequest, response: HttpServletResponse) {
            try {
                task.run()
            } catch (e: TaskException) {
                response.sendError(400, e.message)
            }
        }
    }
}
