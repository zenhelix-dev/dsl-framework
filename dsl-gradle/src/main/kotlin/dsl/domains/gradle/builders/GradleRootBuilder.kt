package dsl.domains.gradle.builders

import core.ast.Node
import core.context.Context
import dsl.domains.gradle.nodes.CreatePublicationNode
import dsl.domains.gradle.nodes.FromComponentNode
import dsl.domains.gradle.nodes.GradleBlockNode
import dsl.domains.gradle.nodes.GradleNode
import dsl.domains.gradle.nodes.MavenLocalNode
import dsl.domains.gradle.nodes.MavenNode
import dsl.domains.gradle.nodes.PublicationNode
import dsl.domains.gradle.nodes.PublicationsNode
import dsl.domains.gradle.nodes.PublishingNode
import dsl.domains.gradle.nodes.RepositoriesNode
import dsl.domains.gradle.nodes.SignPublicationsNode
import dsl.domains.gradle.nodes.SigningNode
import dsl.domains.gradle.nodes.UseInMemoryPgpKeysNode
import dsl.domains.gradle.references.ComponentReference
import dsl.domains.gradle.references.PublicationsReference
import dsl.domains.gradle.references.PublishingReference
import dsl.framework.BaseDSLBuilder

/**
 * Корневой строитель Gradle скрипта
 */
class GradleRootBuilder(
    private val context: Context
) : BaseDSLBuilder<GradleNode>() {

    override fun build(): GradleNode {
        return GradleBlockNode(children.filterIsInstance<GradleNode>())
    }

    fun publishing(block: PublishingBuilder.() -> Unit): GradleNode {
        val builder = PublishingBuilder(context)
        builder.block()
        val node = builder.build()
        addChild(node)
        return node
    }

    fun signing(block: SigningBuilder.() -> Unit): GradleNode {
        val builder = SigningBuilder(context)
        builder.block()
        val node = builder.build()
        addChild(node)
        return node
    }
}

/**
 * Строитель блока publishing
 */
class PublishingBuilder(
    private val context: Context
) : BaseDSLBuilder<PublishingNode>() {

    private lateinit var repositories: RepositoriesNode
    private lateinit var publications: PublicationsNode

    fun repositories(block: RepositoriesBuilder.() -> Unit) {
        val builder = RepositoriesBuilder(context)
        builder.block()
        repositories = builder.build()
        addChild(repositories)
    }

    fun publications(block: PublicationsBuilder.() -> Unit) {
        val builder = PublicationsBuilder(context)
        builder.block()
        publications = builder.build()
        addChild(publications)
    }

    override fun build(): PublishingNode {
        return PublishingNode(repositories, publications)
    }
}

/**
 * Строитель блока repositories
 */
class RepositoriesBuilder(
    private val context: Context
) : BaseDSLBuilder<RepositoriesNode>() {

    fun mavenLocal() {
        addChild(MavenLocalNode())
    }

    fun maven(url: String) {
        addChild(MavenNode(url))
    }

    override fun build(): RepositoriesNode {
        return RepositoriesNode(children)
    }
}

/**
 * Строитель блока publications
 */
class PublicationsBuilder(
    val context: Context // Публичный для inline функций
) : BaseDSLBuilder<PublicationsNode>() {

    inline fun <reified T> create(name: String, block: PublicationBuilder.() -> Unit) {
        val builder = PublicationBuilder(this.context, T::class.simpleName!!)
        builder.block()
        `access$addChild`(CreatePublicationNode(name, T::class.simpleName!!, builder.build()))
    }

    override fun build(): PublicationsNode {
        return PublicationsNode(children)
    }

    @PublishedApi
    internal fun `access$addChild`(child: Node) = addChild(child)
}

/**
 * Строитель конфигурации публикации
 */
class PublicationBuilder(
    private val context: Context,
    private val publicationType: String
) : BaseDSLBuilder<PublicationNode>() {

    fun from(component: ComponentReference) {
        addChild(FromComponentNode(component))
    }

    override fun build(): PublicationNode {
        return PublicationNode(publicationType, children)
    }
}

/**
 * Строитель блока signing
 */
class SigningBuilder(
    private val context: Context
) : BaseDSLBuilder<SigningNode>() {

    val publishing = PublishingReference()

    fun useInMemoryPgpKeys(keyId: String, password: String) {
        addChild(UseInMemoryPgpKeysNode(keyId, password))
    }

    fun sign(publications: PublicationsReference) {
        addChild(SignPublicationsNode(publications))
    }

    override fun build(): SigningNode {
        return SigningNode(children)
    }
}