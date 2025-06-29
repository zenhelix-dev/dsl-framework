package gradle.dsl.core

import kotlin.reflect.KClass

enum class ArgumentType {
    STRING, CODE
}

data class TypedArgument(val value: Any, val type: ArgumentType = ArgumentType.STRING)

internal fun String.asCode(): TypedArgument = TypedArgument(this, ArgumentType.CODE)
internal fun String.asString(): TypedArgument = TypedArgument(this, ArgumentType.STRING)

internal fun KClass<*>.asClassReference(): TypedArgument = TypedArgument("${this.simpleName}::class", ArgumentType.CODE)

internal fun codeValue(value: String): TypedArgument = value.asCode()
internal fun stringValue(value: String): TypedArgument = value.asString()

@PublishedApi
internal fun classRef(clazz: KClass<*>): TypedArgument = clazz.asClassReference()

object FormattingHelper {

    data class FormattedArgument(
        val formatSpecifier: String,
        val value: Any
    )

    fun formatArgument(arg: Any): FormattedArgument = when (arg) {
        is TypedArgument -> FormattedArgument(
            formatSpecifier = if (arg.type == ArgumentType.STRING) {
                "%S"
            } else {
                "%L"
            },
            value = arg.value
        )

        is BaseNamedContainer<*> -> FormattedArgument("%L", arg.`access$proxyPath`)
        is String -> FormattedArgument("%S", arg)
        else -> FormattedArgument("%L", arg)
    }

    fun formatArguments(arguments: List<Any>): Pair<String, Array<Any>> {
        val formatted = arguments.map { formatArgument(it) }
        return formatted.joinToString(", ") { it.formatSpecifier } to formatted.map { it.value }.toTypedArray()
    }

}