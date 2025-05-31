package core.context

import core.types.Type

/**
 * Область видимости символов
 */
interface Scope {
    val name: String
    val parent: Scope?
    fun define(name: String, symbol: Symbol)
    fun resolve(name: String): Symbol?
    fun createChild(name: String): Scope
}

/**
 * Символ в области видимости
 */
data class Symbol(
    val name: String,
    val type: Type,
    val kind: SymbolKind,
    val modifiers: Set<Modifier> = emptySet(),
    val value: Any? = null
)

/**
 * Типы символов
 */
enum class SymbolKind {
    VARIABLE,
    FUNCTION,
    CLASS,
    INTERFACE,
    PROPERTY,
    PARAMETER
}

/**
 * Модификаторы символов
 */
enum class Modifier {
    PUBLIC, PRIVATE, PROTECTED, INTERNAL,
    ABSTRACT, FINAL, OPEN, SEALED,
    VAR, VAL, CONST,
    INLINE, SUSPEND, OPERATOR
}

/**
 * Базовая реализация области видимости
 */
class BaseScope(
    override val name: String,
    override val parent: Scope? = null,
    private val symbols: MutableMap<String, Symbol> = mutableMapOf()
) : Scope {

    override fun define(name: String, symbol: Symbol) {
        if (symbols.containsKey(name)) {
            throw IllegalArgumentException("Symbol '$name' already defined in scope '${this.name}'")
        }
        symbols[name] = symbol
    }

    override fun resolve(name: String): Symbol? {
        return symbols[name] ?: parent?.resolve(name)
    }

    override fun createChild(name: String): Scope {
        return BaseScope(name, this)
    }
}