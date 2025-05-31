package core.ast

/**
 * Базовые абстрактные типы узлов
 */
abstract class ExpressionNode(override val metadata: NodeMetadata) : Node
abstract class StatementNode(override val metadata: NodeMetadata) : Node
abstract class DeclarationNode(override val metadata: NodeMetadata) : Node
abstract class TypeNode(override val metadata: NodeMetadata) : Node

/**
 * Блок операторов
 */
class BlockNode(
    override val metadata: NodeMetadata,
    val statements: List<StatementNode>
) : Node {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = statements
}

/**
 * Литерал (строка, число, булево значение и т.д.)
 */
class LiteralNode(
    override val metadata: NodeMetadata,
    val value: Any,
    val literalType: LiteralType
) : ExpressionNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = emptyList()
}

/**
 * Ссылка на переменную/функцию/тип
 */
class ReferenceNode(
    override val metadata: NodeMetadata,
    val name: String,
    val qualifier: ReferenceNode? = null
) : ExpressionNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = listOfNotNull(qualifier)
}

/**
 * Типы литералов
 */
enum class LiteralType {
    STRING,
    NUMBER,
    BOOLEAN,
    NULL
}