package dsl.framework

import core.ast.Node
import core.context.Context

/**
 * Базовый интерфейс DSL
 */
interface DSL<N : Node> {
    val context: Context
    fun build(block: DSLBuilder<N>.() -> Unit): N
    fun execute(node: N): Any?
    fun generate(node: N): String
}

/**
 * Базовая реализация DSL
 */
abstract class BaseDSL<N : Node, B : DSLBuilder<N>>(
    override val context: Context
) : DSL<N> {

    protected abstract fun createBuilder(): B

    fun buildWith(block: B.() -> Unit): N {
        val builder = createBuilder()
        builder.block()
        return builder.build()
    }

    override fun build(block: DSLBuilder<N>.() -> Unit): N {
        val builder = createBuilder()
        builder.block()
        return builder.build()
    }
}