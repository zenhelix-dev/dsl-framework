package dsl.framework

import core.context.Context

/**
 * Контекст DSL с дополнительной функциональностью
 */
class DSLContext(
    private val baseContext: Context,
    val dslType: DSLType
) : Context by baseContext {

    private val dslReferences = mutableMapOf<String, DSLReference>()

    fun registerReference(name: String, reference: DSLReference) {
        dslReferences[name] = reference
    }

    fun resolveReference(name: String): DSLReference? {
        return dslReferences[name]
    }

    fun createDSLChild(): DSLContext {
        return DSLContext(createChild(), dslType)
    }
}

/**
 * Типы DSL
 */
enum class DSLType {
    GRADLE,
    MAVEN,
    CUSTOM
}

/**
 * Ссылка в DSL контексте
 */
class DSLReference(
    val name: String,
    val type: String,
    val properties: MutableMap<String, DSLReference> = mutableMapOf()
) {
    operator fun get(property: String): DSLReference {
        return properties.getOrPut(property) {
            DSLReference("$name.$property", type)
        }
    }
}