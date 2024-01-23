package com.yutao.flooow.host

import com.yutao.flooow.ScriptWithTask
import com.yutao.flooow.dsl.TaskDSL
import java.io.File
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.constructorArgs
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.host.toScriptSource

class TaskScriptHost {
    private val scriptingHost = BasicJvmScriptingHost()

    fun eval(
        sourceCodeFile: File,
        dsl: TaskDSL
    ) = eval(sourceCodeFile.toScriptSource(), dsl)

    /**
     * Evaluates the given project script [sourceCode] against the
     * given [project].
     */
    fun eval(
        sourceCode: String,
        dsl: TaskDSL
    ) = eval(sourceCode.toScriptSource(), dsl)

    /**
     * Evaluates the given project script [sourceCode] against the
     * given [project].
     */
    fun eval(
        sourceCode: SourceCode,
        dsl: TaskDSL
    ): ResultWithDiagnostics<EvaluationResult> {
        return scriptingHost.evalWithTemplate<ScriptWithTask>(
            sourceCode,
            evaluation = {
                constructorArgs(dsl)
            }
        )
    }

}
