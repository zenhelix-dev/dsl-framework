package gradle.dsl.plugins.embedded

import gradle.dsl.core.BaseNamedContainer
import gradle.dsl.core.DslBodyBlock
import gradle.dsl.core.DslElement
import gradle.dsl.core.FunctionCall

class RepositoryHandlerBlock(parent: DslElement) : BaseNamedContainer<DslBodyBlock>("repositories", parent) {

    fun mavenCentral() = apply {
        addChild(FunctionCall("mavenCentral"))
    }

    fun mavenLocal() = apply {
        addChild(FunctionCall("mavenLocal"))
    }

    fun gradlePluginPortal() = apply {
        addChild(FunctionCall("gradlePluginPortal"))
    }

}