package core.context

import core.types.TypeSystem

/**
 * Контекст выполнения DSL
 */
interface Context {
    val parent: Context?
    fun <T> resolve(key: ContextKey<T>): T?
    fun <T> bind(key: ContextKey<T>, value: T)
    fun createChild(): Context
}

/**
 * Ключ для привязки значений в контексте
 */
class ContextKey<T>(val name: String) {
    companion object {
        val CURRENT_SCOPE = ContextKey<Scope>("currentScope")
        val TYPE_SYSTEM = ContextKey<TypeSystem>("typeSystem")
        val LANGUAGE = ContextKey<Language>("language")
        val IMPORTS = ContextKey<Set<String>>("imports")
    }
}

/**
 * Интерфейс языка программирования
 */
interface Language {
    val name: String
    val features: Set<LanguageFeature>
}

/**
 * Возможности языка программирования
 */
enum class LanguageFeature {
    TYPE_INFERENCE,
    SMART_CASTS,
    EXTENSION_FUNCTIONS,
    OPERATOR_OVERLOADING,
    NULLABLE_TYPES,
    GROOVY_CLOSURES,
    GRADLE_PLUGINS
}

/**
 * Базовая реализация контекста
 */
class BaseContext(
    override val parent: Context? = null,
    private val bindings: MutableMap<ContextKey<*>, Any> = mutableMapOf()
) : Context {

    @Suppress("UNCHECKED_CAST")
    override fun <T> resolve(key: ContextKey<T>): T? {
        return bindings[key] as? T ?: parent?.resolve(key)
    }

    override fun <T> bind(key: ContextKey<T>, value: T) {
        bindings[key] = value as Any
    }

    override fun createChild(): Context {
        return BaseContext(parent = this)
    }
}