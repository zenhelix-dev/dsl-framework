package languages.kotlin.ast

import core.ast.BlockNode
import core.ast.DeclarationNode
import core.ast.ExpressionNode
import core.ast.Node
import core.ast.NodeMetadata
import core.ast.NodeVisitor
import core.ast.TypeNode
import core.context.Modifier

/**
 * Kotlin класс
 */
class KotlinClassNode(
    metadata: NodeMetadata,
    val name: String,
    val modifiers: Set<Modifier>,
    val typeParameters: List<KotlinTypeParameterNode>,
    val superTypes: List<TypeNode>,
    val members: List<KotlinMemberNode>
) : DeclarationNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = typeParameters + superTypes + members
}

/**
 * Kotlin функция
 */
class KotlinFunctionNode(
    metadata: NodeMetadata,
    val name: String,
    val modifiers: Set<Modifier>,
    val typeParameters: List<KotlinTypeParameterNode>,
    val parameters: List<KotlinParameterNode>,
    val returnType: TypeNode?,
    val body: BlockNode?
) : KotlinMemberNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> =
        typeParameters + parameters + listOfNotNull(returnType, body)
}

/**
 * Kotlin свойство
 */
class KotlinPropertyNode(
    metadata: NodeMetadata,
    val name: String,
    val modifiers: Set<Modifier>,
    val type: TypeNode?,
    val initializer: ExpressionNode?,
    val getter: KotlinPropertyAccessorNode?,
    val setter: KotlinPropertyAccessorNode?
) : KotlinMemberNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> =
        listOfNotNull(type, initializer, getter, setter)
}

/**
 * Базовый класс для членов Kotlin класса
 */
abstract class KotlinMemberNode(metadata: NodeMetadata) : DeclarationNode(metadata)

/**
 * Параметр Kotlin функции
 */
class KotlinParameterNode(
    metadata: NodeMetadata,
    val name: String,
    val type: TypeNode,
    val defaultValue: ExpressionNode?,
    val modifiers: Set<Modifier>
) : Node {
    override val metadata: NodeMetadata = metadata
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = listOfNotNull(type, defaultValue)
}

/**
 * Параметр типа Kotlin
 */
class KotlinTypeParameterNode(
    metadata: NodeMetadata,
    val name: String,
    val constraints: List<TypeNode>
) : TypeNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = constraints
}

/**
 * Аксессор свойства (getter/setter)
 */
class KotlinPropertyAccessorNode(
    metadata: NodeMetadata,
    val isGetter: Boolean,
    val body: BlockNode?
) : Node {
    override val metadata: NodeMetadata = metadata
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = listOfNotNull(body)
}

/**
 * Kotlin лямбда выражение
 */
class KotlinLambdaNode(
    metadata: NodeMetadata,
    val parameters: List<KotlinParameterNode>,
    val body: Node
) : ExpressionNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = parameters + listOf(body)
}

/**
 * Вызов функции в Kotlin
 */
class KotlinCallNode(
    metadata: NodeMetadata,
    val receiver: ExpressionNode?,
    val name: String,
    val typeArguments: List<TypeNode>,
    val arguments: List<ExpressionNode>,
    val lambdaArgument: KotlinLambdaNode?
) : ExpressionNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> =
        listOfNotNull(receiver) + typeArguments + arguments + listOfNotNull(lambdaArgument)
}

/**
 * Доступ по индексу в Kotlin
 */
class KotlinIndexAccessNode(
    metadata: NodeMetadata,
    val receiver: ExpressionNode,
    val indices: List<ExpressionNode>
) : ExpressionNode(metadata) {
    override fun accept(visitor: NodeVisitor): Any? = visitor.visit(this)
    override fun children(): List<Node> = listOf(receiver) + indices
}