package gradle.dsl.core

import kotlin.reflect.KClass

abstract class BaseNamedContainer<D : DslBodyBlock>(
    blockName: String,
    open val providers: Map<KClass<out D>, DslProvider<out D>> = emptyMap()
) : DslBlock(blockName) {

    fun create(name: String) = apply {
        addChild(FunctionCall("create", listOf(name)))
    }

    inline fun <reified U : D> create(name: String, noinline block: (U.() -> Unit)? = null) = apply {
        val dsl = block?.let { getProvider<U>().createDsl().apply(it) }
        addChild(FunctionCall("create", listOf(name), dsl))
    }

    fun register(name: String) = apply {
        addChild(FunctionCall("register", listOf(name)))
    }

    inline fun <reified U : D> register(name: String, noinline block: (U.() -> Unit)? = null) = apply {
        val dsl = block?.let { getProvider<U>().createDsl().apply(it) }
        addChild(FunctionCall("register", listOf(name), dsl))
    }

    fun named(name: String) = apply {
        addChild(FunctionCall("named", listOf(name)))
    }

    inline fun <reified U : D> named(name: String, noinline block: (U.() -> Unit)? = null) = apply {
        val dsl = block?.let { getProvider<U>().createDsl().apply(it) }
        addChild(FunctionCall("named", listOf(name), dsl))
    }

    fun getByName(name: String) = apply {
        addChild(FunctionCall("getByName", listOf(name)))
    }

    inline fun <reified U : D> getByName(name: String, noinline block: (U.() -> Unit)? = null) = apply {
        val dsl = block?.let { getProvider<U>().createDsl().apply(it) }
        addChild(FunctionCall("getByName", listOf(name), dsl))
    }

    @PublishedApi
    internal inline fun <reified U : D> getProvider(): DslProvider<U> {
        @Suppress("UNCHECKED_CAST")
        return providers[U::class] as? DslProvider<U> ?: throw IllegalArgumentException("No DSL provider for type ${U::class}")
    }
}

abstract class BasePolymorphicContainer<D : DslBodyBlock>(
    blockName: String,
    override val providers: Map<KClass<out D>, DslProvider<out D>> = emptyMap()
) : BaseNamedContainer<D>(blockName, providers) {

    fun create(name: String, type: KClass<out D>) = apply {
        addChild(FunctionCall("create", listOf(name, "${type.simpleName}::class")))
    }

    inline fun <reified U : D> create(name: String, type: KClass<out U>, noinline block: (U.() -> Unit)? = null) = apply {
        val dsl = block?.let { getProvider<U>().createDsl().apply(it) }
        addChild(FunctionCall("create", listOf(name, "${type.simpleName}::class"), dsl))
    }

    fun register(name: String, type: KClass<out D>) = apply {
        addChild(FunctionCall("register", listOf(name, "${type.simpleName}::class")))
    }

    inline fun <reified U : D> register(name: String, type: KClass<out U>, noinline block: (U.() -> Unit)? = null) = apply {
        val dsl = block?.let { getProvider<U>().createDsl().apply(it) }
        addChild(FunctionCall("register", listOf(name, "${type.simpleName}::class"), dsl))
    }

}