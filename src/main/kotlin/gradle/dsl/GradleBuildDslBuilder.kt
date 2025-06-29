package gradle.dsl

import gradle.dsl.core.AutoRegisterContext
import gradle.dsl.core.DslElement
import gradle.dsl.core.PropertyAssignment
import gradle.dsl.plugins.embedded.PluginsDependenciesSpecScopeBlock
import gradle.dsl.plugins.embedded.ProjectBlock
import gradle.dsl.plugins.embedded.PublishingExtensionBlock
import gradle.dsl.plugins.embedded.PublishingProxy
import gradle.dsl.plugins.embedded.RepositoryHandlerBlock
import gradle.dsl.plugins.embedded.SigningExtensionBlock
import gradle.dsl.plugins.embedded.TasksBlock

class GradleBuildDslBuilder : AbstractScriptBuilder("build.gradle.kts") {

    private val rootProject = ProjectBlock("")

    var group: String
        get() = throw UnsupportedOperationException("group is write-only in DSL context")
        set(value) {
            elements.add(PropertyAssignment("group", value))
        }

    fun plugins(block: PluginsDependenciesSpecScopeBlock.() -> Unit = {}) = apply {
        elements.add(PluginsDependenciesSpecScopeBlock().apply(block))
    }

    fun subprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        elements.add(ProjectBlock("subprojects").apply(block))
    }

    fun tasks(block: TasksBlock.() -> Unit = {}) = apply {
        elements.add(TasksBlock().apply(block))
    }

    fun repositories(block: RepositoryHandlerBlock.() -> Unit = {}) = apply {
        elements.add(RepositoryHandlerBlock().apply(block))
    }

    val publishing: PublishingProxy = PublishingProxy(object : AutoRegisterContext {
        override fun autoRegister(element: DslElement) {
            elements.add(element)
        }
    })

    fun publishing(block: PublishingExtensionBlock.() -> Unit = {}) = apply {
        elements.add(PublishingExtensionBlock(object : AutoRegisterContext {
            override fun autoRegister(element: DslElement) {
                elements.add(element)
            }
        }).apply(block))
    }

    fun signing(block: SigningExtensionBlock.() -> Unit = {}) = apply {
        elements.add(SigningExtensionBlock().apply(block))
    }

}
