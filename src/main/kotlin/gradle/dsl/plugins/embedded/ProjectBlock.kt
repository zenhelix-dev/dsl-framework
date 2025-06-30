package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock
import gradle.dsl.core.DslElement
import gradle.dsl.core.PropertyAssignment

class ProjectBlock(override val blockName: String = "", parent: DslElement?) : DslBlock(blockName, parent) {

    var group: String
        get() = throw UnsupportedOperationException("group is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("group", value))
        }

    var version: String
        get() = throw UnsupportedOperationException("version is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("version", value))
        }

    fun plugins(block: PluginsDependenciesSpecScopeBlock.() -> Unit = {}) = apply {
        addChild(PluginsDependenciesSpecScopeBlock(this).apply(block))
    }

    fun subprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        addChild(ProjectBlock("subprojects", this).apply(block))
    }

    fun tasks(block: TasksBlock.() -> Unit = {}) = apply {
        addChild(TasksBlock(this).apply(block))
    }

    fun repositories(block: RepositoryHandlerBlock.() -> Unit = {}) = apply {
        addChild(RepositoryHandlerBlock(this).apply(block))
    }

    val publishing: PublishingProxy = PublishingProxy(this, this)

    fun publishing(block: PublishingExtensionBlock.() -> Unit = {}) = apply {
        addChild(PublishingExtensionBlock(this).apply(block))
    }

    fun signing(block: SigningExtensionBlock.() -> Unit = {}) = apply {
        addChild(SigningExtensionBlock(this).apply(block))
    }

}