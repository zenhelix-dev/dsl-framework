package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock
import gradle.dsl.core.PropertyAssignment

class ProjectBlock(override val blockName: String = "") : DslBlock(blockName) {

    var group: String
        get() = throw UnsupportedOperationException("group is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("group", value))
        }

    fun tasks(block: TasksBlock.() -> Unit = {}) = apply {
        addChild(TasksBlock().apply(block))
    }

    fun repositories(block: RepositoryHandlerBlock.() -> Unit = {}) = apply {
        addChild(RepositoryHandlerBlock().apply(block))
    }

    val publishing: PublishingProxy = PublishingProxy(this)

    fun publishing(block: PublishingExtensionBlock.() -> Unit = {}) = apply {
        addChild(PublishingExtensionBlock(this).apply(block))
    }

    fun signing(block: SigningExtensionBlock.() -> Unit = {}) = apply {
        addChild(SigningExtensionBlock().apply(block))
    }

}