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

    fun publishing(block: PublishingExtensionBlock.() -> Unit = {}) = apply {
        addChild(PublishingExtensionBlock().apply(block))
    }

    fun signing(block: SigningExtensionBlock.() -> Unit = {}) = apply {
        children.add(SigningExtensionBlock().apply(block))
    }

}