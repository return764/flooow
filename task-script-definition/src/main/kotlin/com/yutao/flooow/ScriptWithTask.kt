package com.yutao.flooow

import com.yutao.flooow.dsl.TaskDSL
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.ScriptAcceptedLocation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.acceptedLocations
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.ide
import kotlin.script.experimental.api.refineConfiguration
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

@KotlinScript(
    // File extension for the script type
    fileExtension = "task.kts",
    // Compilation configuration for the script type
    compilationConfiguration = ScriptWithTaskConfiguration::class
)
abstract class ScriptWithTask(dsl: TaskDSL) : TaskDSL by dsl

object ScriptWithTaskConfiguration : ScriptCompilationConfiguration(
    {
        defaultImports(
            "com.yutao.flooow.dsl.*",
            "com.yutao.flooow.enums.TaskType"
        )
        // Implicit imports for all scripts of this type
        jvm {
            // Extract the whole classpath from context classloader and use it as dependencies
            dependenciesFromCurrentContext(
                wholeClasspath = true
            )
        }
        ide {
            acceptedLocations(ScriptAcceptedLocation.Everywhere)
        }
        // Callbacks
        refineConfiguration {
        }

    }
)

//class ScriptIdeDefinitions : ScriptDefinitionsProvider {
//
//}
