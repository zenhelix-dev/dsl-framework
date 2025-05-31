package core.ast

/**
 * Интерфейс посетителя для обхода AST
 */
interface NodeVisitor {
    fun visit(node: Node): Any?

    fun visitChildren(node: Node): List<Any?> {
        return node.children().map { it.accept(this) }
    }
}

/**
 * Базовая реализация посетителя с методами для каждого типа узла
 */
abstract class BaseNodeVisitor : NodeVisitor {
    override fun visit(node: Node): Any? {
        return when (node) {
            is LiteralNode -> visitLiteral(node)
            is ReferenceNode -> visitReference(node)
            is ExpressionNode -> visitExpression(node)
            is StatementNode -> visitStatement(node)
            is DeclarationNode -> visitDeclaration(node)
            is BlockNode -> visitBlock(node)
            is TypeNode -> visitType(node)
            else -> visitDefault(node)
        }
    }

    protected open fun visitExpression(node: ExpressionNode): Any? = visitDefault(node)
    protected open fun visitStatement(node: StatementNode): Any? = visitDefault(node)
    protected open fun visitDeclaration(node: DeclarationNode): Any? = visitDefault(node)
    protected open fun visitBlock(node: BlockNode): Any? = visitDefault(node)
    protected open fun visitLiteral(node: LiteralNode): Any? = visitDefault(node)
    protected open fun visitReference(node: ReferenceNode): Any? = visitDefault(node)
    protected open fun visitType(node: TypeNode): Any? = visitDefault(node)
    protected open fun visitDefault(node: Node): Any? = null
}