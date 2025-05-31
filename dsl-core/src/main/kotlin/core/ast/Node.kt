package core.ast

/**
 * Базовый интерфейс для всех узлов AST
 */
interface Node {
    val metadata: NodeMetadata
    fun accept(visitor: NodeVisitor): Any?
    fun children(): List<Node>
}

/**
 * Метаданные узла AST
 */
data class NodeMetadata(
    val sourceLocation: SourceLocation? = null,
    val type: NodeType,
    val attributes: Map<String, Any> = emptyMap()
)

/**
 * Позиция в исходном коде
 */
data class SourceLocation(
    val file: String,
    val line: Int,
    val column: Int
)

/**
 * Типы узлов AST
 */
enum class NodeType {
    EXPRESSION,
    STATEMENT,
    DECLARATION,
    BLOCK,
    LITERAL,
    REFERENCE,
    TYPE
}