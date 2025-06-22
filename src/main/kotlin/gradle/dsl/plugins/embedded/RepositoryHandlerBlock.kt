package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock
import gradle.dsl.core.FunctionCall

class RepositoryHandlerBlock : DslBlock("repositories") {

    fun mavenCentral(): RepositoryHandlerBlock {
        addChild(FunctionCall("mavenCentral"))
        return this
    }

    fun mavenLocal(): RepositoryHandlerBlock {
        addChild(FunctionCall("mavenLocal"))
        return this
    }

    fun gradlePluginPortal(): RepositoryHandlerBlock {
        addChild(FunctionCall("gradlePluginPortal"))
        return this
    }

}