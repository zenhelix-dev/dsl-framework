package languages.groovy.ast

import core.ast.BlockNode
import core.ast.DeclarationNode
import core.ast.ExpressionNode
import core.ast.Node
import core.ast.NodeMetadata
import core.ast.NodeVisitor
import core.ast.TypeNode
import core.context.Modifier

/**
 * Groovy класс
 */
class GroovyClassNode(
    metadata: NodeMetadata,
    val name: String,
    val modifiers: Set<Modifier>,
    val superTypes: List<TypeNode>,
    val members: List<GroovyMemberNode>
) : DeclarationNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = superTypes + members
}

/**
 * Groovy метод
 */
class GroovyMethodNode(
    metadata: NodeMetadata,
    val name: String,
    val modifiers: Set<Modifier>,
    val parameters: List<GroovyParameterNode>,
    val returnType: TypeNode?,
    val body: BlockNode?
) : GroovyMemberNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = parameters + listOfNotNull(returnType, body)
}

/**
 * Groovy свойство/поле
 */
class GroovyFieldNode(
    metadata: NodeMetadata,
    val name: String,
    val modifiers: Set<Modifier>,
    val type: TypeNode?,
    val initializer: ExpressionNode?
) : GroovyMemberNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = listOfNotNull(type, initializer)
}

/**
 * Базовый класс для членов Groovy класса
 */
abstract class GroovyMemberNode(metadata: NodeMetadata) : DeclarationNode(metadata)

/**
 * Параметр Groovy метода
 */
class GroovyParameterNode(
    metadata: NodeMetadata,
    val name: String,
    val type: TypeNode?,
    val defaultValue: ExpressionNode?
) : Node {
    override val metadata: NodeMetadata = metadata
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = listOfNotNull(type, defaultValue)
}

/**
 * Groovy замыкание (closure)
 */
class GroovyClosureNode(
    metadata: NodeMetadata,
    val parameters: List<GroovyParameterNode>,
    val body: Node
) : ExpressionNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = parameters + listOf(body)
}

/**
 * Вызов метода в Groovy
 */
class GroovyMethodCallNode(
    metadata: NodeMetadata,
    val receiver: ExpressionNode?,
    val name: String,
    val arguments: List<ExpressionNode>,
    val closureArgument: GroovyClosureNode?
) : ExpressionNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> =
        listOfNotNull(receiver) + arguments + listOfNotNull(closureArgument)
}

/**
 * Доступ к свойству в Groovy
 */
class GroovyPropertyAccessNode(
    metadata: NodeMetadata,
    val receiver: ExpressionNode,
    val propertyName: String
) : ExpressionNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = listOf(receiver)
}

/**
 * Строковая интерполяция в Groovy
 */
class GroovyStringInterpolationNode(
    metadata: NodeMetadata,
    val parts: List<StringPart>
) : ExpressionNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = parts.filterIsInstance<StringPart.Expression>().map { it.expression }
}

/**
 * Часть интерполированной строки
 */
sealed class StringPart {
    data class Text(val value: String) : StringPart()
    data class Expression(val expression: ExpressionNode) : StringPart()
}