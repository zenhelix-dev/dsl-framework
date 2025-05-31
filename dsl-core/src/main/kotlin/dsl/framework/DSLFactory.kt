package dsl.framework

import core.context.Context
import core.generation.CodeGenerator

/**
 * Интерфейс фабрики DSL
 */
interface DSLFactory<D : DSL<*>> {
    fun create(context: Context): D
    fun getCodeGenerator(): CodeGenerator
}

/**
 * Базовая реализация фабрики DSL
 */
abstract class BaseDSLFactory<D : DSL<*>> : DSLFactory<D> {

    protected abstract fun createDSL(context: DSLContext): D

    override fun create(context: Context): D {
        val dslContext = DSLContext(context, getDSLType())
        return createDSL(dslContext)
    }

    protected abstract fun getDSLType(): DSLType
}