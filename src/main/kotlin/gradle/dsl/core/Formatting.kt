package gradle.dsl.core

import com.squareup.kotlinpoet.CodeBlock
import kotlin.reflect.KClass

enum class ArgumentType {
    STRING, CODE
}

data class TypedArgument(val value: Any, val type: ArgumentType = ArgumentType.STRING)

data class VariableReference(val name: String) : DslElement {
    override fun toCodeBlock(): CodeBlock = CodeBlock.of("%L", name)
}

data class VariableDeclaration(
    val name: String,
    val value: Any,
    val type: String? = null,
    val isNullable: Boolean = false,
    val isMutable: Boolean = false
) : DslElement {
    
    override fun toCodeBlock(): CodeBlock {
        val keyword = if (isMutable) "var" else "val"
        val typeSpec = when {
            type != null -> ": $type${if (isNullable) "?" else ""}"
            else -> ""
        }
        
        val (formatSpec, formattedValue) = when (value) {
            is String -> "%S" to value
            is VariableReference -> "%L" to value.name
            else -> "%L" to value
        }
        
        return CodeBlock.of("$keyword $name$typeSpec = $formatSpec\n", formattedValue)
    }
}

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

    fun formatArgument(arg: Any?): FormattedArgument = when (arg) {
        null -> FormattedArgument("%L", "null")
        is TypedArgument -> FormattedArgument(
            formatSpecifier = if (arg.type == ArgumentType.STRING) "%S" else "%L",
            value = arg.value
        )
        is VariableReference -> FormattedArgument("%L", arg.name)
        is DslProxy -> FormattedArgument("%L", arg.proxyPath)
        is String -> FormattedArgument("%S", arg)
        else -> FormattedArgument("%L", arg)
    }

    fun formatArguments(arguments: List<Any?>): Pair<String, Array<Any>> {
        val formatted = arguments.map { formatArgument(it) }
        return formatted.joinToString(", ") { it.formatSpecifier } to formatted.map { it.value }.toTypedArray()
    }
}

fun ref(name: String): VariableReference = VariableReference(name)

fun valDecl(name: String, value: Any, type: String? = null, nullable: Boolean = false): VariableDeclaration {
    return VariableDeclaration(name, value, type, nullable, false)
}

fun varDecl(name: String, value: Any, type: String? = null, nullable: Boolean = false): VariableDeclaration {
    return VariableDeclaration(name, value, type, nullable, true)
}

class DslVariable(
    private val name: String,
    private val declaration: VariableDeclaration
) {
    operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): VariableReference {
        return VariableReference(name)
    }
}

fun dslVal(name: String, value: Any, type: String? = null, nullable: Boolean = false): DslVariable {
    return DslVariable(name, VariableDeclaration(name, value, type, nullable, false))
}

fun dslVar(name: String, value: Any, type: String? = null, nullable: Boolean = false): DslVariable {
    return DslVariable(name, VariableDeclaration(name, value, type, nullable, true))
}