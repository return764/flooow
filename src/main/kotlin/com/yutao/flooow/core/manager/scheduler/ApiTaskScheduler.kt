package com.yutao.flooow.core.manager.scheduler

import com.yutao.flooow.core.exception.TaskException
import com.yutao.flooow.core.manager.ExecutableTask
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

@Component
class ApiTaskScheduler(
    val apiRegister: RequestMappingHandlerMapping
) {
    private val prefix = "/graph"
    val requestMappings = mutableMapOf<String, RequestMappingInfo>()

    fun cancel(task: ExecutableTask) {
        requestMappings[task.identify.fileName]?.let { apiRegister.unregisterMapping(it) }
    }

    fun refresh(task: ExecutableTask) {

    }

    fun schedule(task: ExecutableTask) {
        cancel(task)
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
