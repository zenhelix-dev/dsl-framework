package dsl.framework

import core.ast.Node

/**
 * Интерфейс строителя DSL
 */
interface DSLBuilder<N : Node> {
    fun build(): N
}

/**
 * Базовая реализация строителя DSL
 */
abstract class BaseDSLBuilder<N : Node> : DSLBuilder<N> {
    protected val children = mutableListOf<Node>()

    protected fun addChild(child: Node) {
        children.add(child)
    }
}