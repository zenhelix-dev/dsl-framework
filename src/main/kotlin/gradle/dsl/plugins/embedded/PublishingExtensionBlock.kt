package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock

class PublishingExtensionBlock : DslBlock("publishing") {

    fun repositories(block: RepositoryHandlerBlock.() -> Unit = {}) = apply {
        addChild(RepositoryHandlerBlock().apply(block))
    }

    fun publication(block: PublicationsContainerBlock.() -> Unit = {}) = apply {
        addChild(PublicationsContainerBlock().apply(block))
    }

}

class PublicationsContainerBlock : DslBlock("publications") {

}