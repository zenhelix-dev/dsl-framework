package dsl.domains.gradle.nodes

import core.ast.Node
import core.ast.NodeMetadata
import core.ast.NodeType
import core.ast.NodeVisitor
import dsl.domains.gradle.references.ComponentReference
import dsl.domains.gradle.references.PublicationsReference

/**
 * Базовый интерфейс для всех Gradle узлов
 */
interface GradleNode : Node

/**
 * Блок Gradle скрипта
 */
class GradleBlockNode(
    val statements: List<GradleNode>
) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.BLOCK)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = statements
}

/**
 * Блок publishing
 */
class PublishingNode(
    val repositories: RepositoriesNode,
    val publications: PublicationsNode
) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.BLOCK)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = listOf(repositories, publications)
}

/**
 * Блок repositories
 */
class RepositoriesNode(
    val repositories: List<Node>
) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.BLOCK)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = repositories
}

/**
 * Блок publications
 */
class PublicationsNode(
    val publications: List<Node>
) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.BLOCK)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = publications
}

/**
 * Создание публикации
 */
class CreatePublicationNode(
    val name: String,
    val type: String,
    val configuration: PublicationNode
) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.STATEMENT)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = listOf(configuration)
}

/**
 * Конфигурация публикации
 */
class PublicationNode(
    val publicationType: String,
    val configurations: List<Node>
) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.BLOCK)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = configurations
}

/**
 * Maven Local репозиторий
 */
class MavenLocalNode : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.STATEMENT)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = emptyList()
}

/**
 * Maven репозиторий
 */
class MavenNode(val url: String) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.STATEMENT)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = emptyList()
}

/**
 * Настройка from component
 */
class FromComponentNode(val component: ComponentReference) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.STATEMENT)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = emptyList()
}

/**
 * Блок signing
 */
class SigningNode(
    val configurations: List<Node>
) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.BLOCK)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = configurations
}

/**
 * Настройка in-memory PGP ключей
 */
class UseInMemoryPgpKeysNode(
    val keyId: String,
    val password: String
) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.STATEMENT)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = emptyList()
}

/**
 * Подписание публикаций
 */
class SignPublicationsNode(
    val publications: PublicationsReference
) : GradleNode {
    override val metadata: NodeMetadata = NodeMetadata(type = NodeType.STATEMENT)
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = emptyList()
}