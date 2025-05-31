package core.ast

/**
 * Интерфейс для трансформации узлов AST
 */
interface NodeTransformer {
    fun transform(node: Node): Node
}

/**
 * Базовая реализация трансформатора
 */
abstract class BaseNodeTransformer : NodeTransformer {
    override fun transform(node: Node): Node {
        return when (node) {
            is LiteralNode -> transformLiteral(node)
            is ReferenceNode -> transformReference(node)
            is ExpressionNode -> transformExpression(node)
            is StatementNode -> transformStatement(node)
            is DeclarationNode -> transformDeclaration(node)
            is BlockNode -> transformBlock(node)
            is TypeNode -> transformType(node)
            else -> transformDefault(node)
        }
    }

    protected open fun transformExpression(node: ExpressionNode): Node = transformDefault(node)
    protected open fun transformStatement(node: StatementNode): Node = transformDefault(node)
    protected open fun transformDeclaration(node: DeclarationNode): Node = transformDefault(node)
    protected open fun transformBlock(node: BlockNode): Node = transformDefault(node)
    protected open fun transformLiteral(node: LiteralNode): Node = transformDefault(node)
    protected open fun transformReference(node: ReferenceNode): Node = transformDefault(node)
    protected open fun transformType(node: TypeNode): Node = transformDefault(node)
    protected open fun transformDefault(node: Node): Node = node
}