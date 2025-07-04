package gradle.dsl.plugins.embedded

import gradle.dsl.core.AutoRegisterContext
import gradle.dsl.core.BasePolymorphicContainer
import gradle.dsl.core.DslBlock
import gradle.dsl.core.DslBodyBlock
import gradle.dsl.core.DslElement
import gradle.dsl.core.DslProvider
import gradle.dsl.core.FunctionCall
import gradle.dsl.core.PropertyAssignment

class PublishingExtensionBlock(
    parent: DslElement,
    private val autoRegisterContext: AutoRegisterContext? = null
) : DslBlock("publishing", parent) {

    fun repositories(block: RepositoryHandlerBlock.() -> Unit = {}) = apply {
        addChild(RepositoryHandlerBlock(this).apply(block))
    }

    val publishing: PublishingProxy = PublishingProxy(this, object : AutoRegisterContext {
        override fun autoRegister(element: DslElement) {
            this@PublishingExtensionBlock.addChild(element)
        }
    })

    fun publications(block: PublicationsContainerBlock.() -> Unit = {}) = apply {
        addChild(PublicationsContainerBlock(this, autoRegisterContext).apply(block))
    }

}

class PublishingProxy(parent: DslElement, parentContext: AutoRegisterContext? = null) {

    val publications: PublicationsContainerBlock = PublicationsContainerBlock(
        parent = parent,
        autoRegisterContext = parentContext,
        explicitProxyPath = "publishing.publications"
    )

}

class PublicationsContainerBlock(
    parent: DslElement,
    autoRegisterContext: AutoRegisterContext? = null,
    explicitProxyPath: String? = null
) : BasePolymorphicContainer<PublicationDsl>(
    blockName = "publications",
    parent = parent,
    explicitProxyPath = explicitProxyPath,
    autoRegisterContext = autoRegisterContext,
    providers = mapOf(
        MavenPublication::class to object : DslProvider<MavenPublication> {
            override fun createDsl() = MavenPublication()
        },
        IvyPublication::class to object : DslProvider<IvyPublication> {
            override fun createDsl() = IvyPublication()
        }
    )
) {

    fun maven(name: String, block: (MavenPublication.() -> Unit)? = {}) = apply {
        create<MavenPublication>(name, block)
    }

    fun ivy(name: String, block: (IvyPublication.() -> Unit)? = {}) = apply {
        create<IvyPublication>(name, block)
    }

}

open class PublicationDsl : DslBodyBlock("")

class MavenPublication : PublicationDsl() {

    var groupId: String
        get() = throw UnsupportedOperationException("groupId is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("groupId", value))
        }

    var artifactId: String
        get() = throw UnsupportedOperationException("artifactId is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("artifactId", value))
        }

    var version: String
        get() = throw UnsupportedOperationException("version is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("version", value))
        }

    fun from(component: String) = apply {
        addChild(FunctionCall("from", listOf(component)))
    }

    fun artifact(source: String) = apply {
        addChild(FunctionCall("artifact", listOf(source)))
    }

    fun pom(block: PomDsl.() -> Unit) = apply {
        addChild(PomDsl(this).apply(block))
    }
}

class IvyPublication : PublicationDsl() {

    var organisation: String
        get() = throw UnsupportedOperationException("organisation is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("organisation", value))
        }

    var module: String
        get() = throw UnsupportedOperationException("module is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("module", value))
        }

    var revision: String
        get() = throw UnsupportedOperationException("revision is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("revision", value))
        }

    fun from(component: String) = apply {
        addChild(FunctionCall("from", listOf(component)))
    }

    fun artifact(source: String) = apply {
        addChild(FunctionCall("artifact", listOf(source)))
    }

    fun descriptor(block: IvyDescriptorDsl.() -> Unit) = apply {
        addChild(IvyDescriptorDsl(this).apply(block))
    }
}

class PomDsl(parent: DslElement) : DslBlock("pom", parent) {

    var name: String
        get() = throw UnsupportedOperationException("name is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("name", value))
        }

    var description: String
        get() = throw UnsupportedOperationException("description is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("description", value))
        }

    var url: String
        get() = throw UnsupportedOperationException("url is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("url", value))
        }
}

class IvyDescriptorDsl(parent: DslElement) : DslBlock("descriptor", parent) {

    var status: String
        get() = throw UnsupportedOperationException("status is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("status", value))
        }

    var branch: String
        get() = throw UnsupportedOperationException("branch is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("branch", value))
        }

}