package gradle.dsl.core

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent
import gradle.dsl.core.FormattingHelper.formatArgument
import gradle.dsl.core.FormattingHelper.formatArguments
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

interface DslElement {
    fun toCodeBlock(): CodeBlock
}

interface DslContainer : DslElement {
    val parent: DslElement?
    val children: MutableList<DslElement>

    fun addChild(element: DslElement) {
        children.add(element)
    }

    fun findRoot(): DslContainer {
        var current: DslElement? = this
        var lastContainer: DslContainer = this

        while (current != null) {
            if (current is DslContainer) {
                lastContainer = current
                current = current.parent
            } else {
                break
            }
        }

        return lastContainer
    }

    fun findImportAware(): ImportAware? {
        var current: DslElement? = this
        while (current != null) {
            if (current is ImportAware) {
                return current
            }
            if (current is DslContainer) {
                current = current.parent
            } else {
                break
            }
        }
        return null
    }
}

abstract class DslBlock(
    open val blockName: String,
    override val parent: DslElement?,
    override val children: MutableList<DslElement> = mutableListOf()
) : DslContainer, AutoRegisterContext {

    override fun toCodeBlock(): CodeBlock = buildCodeBlock {
        if (parent == null) {
            children.forEach { add("%L", it.toCodeBlock()) }
        } else {
            beginControlFlow(blockName)
            withIndent {
                children.forEach { add("%L", it.toCodeBlock()) }
            }
            endControlFlow()
        }
    }

    override fun autoRegister(element: DslElement) {
        addChild(element)
    }
}

abstract class DslBodyBlock(
    blockName: String,
    parent: DslElement? = null,
    override val children: MutableList<DslElement> = mutableListOf()
) : DslBlock(blockName, parent, children) {

    override fun toCodeBlock(): CodeBlock = buildCodeBlock {
        withIndent {
            children.forEach { add("%L", it.toCodeBlock()) }
        }
    }
}

class FunctionCall(
    private val name: String,
    private val arguments: List<Any?> = emptyList(),
    private val body: DslBodyBlock? = null
) : DslElement {

    override fun toCodeBlock(): CodeBlock {
        val (formatSpecifiers, values) = formatArguments(arguments)

        val builder = CodeBlock.builder()

        if (body != null) {
            builder
                .beginControlFlow("$name($formatSpecifiers)", *values)
                .add(body.toCodeBlock())
                .endControlFlow()
        } else {
            builder.add("$name($formatSpecifiers)\n", *values)
        }

        return builder.build()
    }
}

data class PropertyAssignment(val name: String, val value: Any) : DslElement {
    override fun toCodeBlock(): CodeBlock {
        val (formatSpecifier, value) = formatArgument(value)
        return CodeBlock.of("%L = $formatSpecifier\n", name, value)
    }
}

data class VariableElement(val value: Any) : DslElement {
    override fun toCodeBlock(): CodeBlock {
        val (formatSpecifier, value) = formatArgument(TypedArgument(value, ArgumentType.CODE))
        return CodeBlock.of("$formatSpecifier\n", value)
    }
}

interface VariableReference {
    val variableName: String
}

@JvmInline
value class ProjectProperty(override val variableName: String) : VariableReference {
    override fun toString(): String = variableName
}

data class DelegatedPropertyDeclaration(
    val name: String,
    val type: String,
    val delegate: String
) : DslElement {
    override fun toCodeBlock(): CodeBlock = CodeBlock.of("val %L: %L by %L\n", name, type, delegate)
}

class ProjectDelegateProvider(private val container: DslContainer) {

    operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>): ProjectDelegateAccessor {
        val typeName = formatTypeName(prop.returnType)
        container.addChild(DelegatedPropertyDeclaration(prop.name, typeName, "project"))
        return ProjectDelegateAccessor(prop.name)
    }

    private fun formatTypeName(kType: kotlin.reflect.KType): String {
        val classifier = kType.classifier
        val isNullable = kType.isMarkedNullable

        return when (classifier) {
            String::class -> if (isNullable) "String?" else "String"
            Int::class -> if (isNullable) "Int?" else "Int"
            Boolean::class -> if (isNullable) "Boolean?" else "Boolean"
            Long::class -> if (isNullable) "Long?" else "Long"
            Double::class -> if (isNullable) "Double?" else "Double"
            Float::class -> if (isNullable) "Float?" else "Float"
            List::class -> {
                val typeArg = kType.arguments.firstOrNull()?.type?.jvmErasure?.simpleName ?: "String"
                if (isNullable) "List<$typeArg>?" else "List<$typeArg>"
            }
            else -> {
                val simpleName = (classifier as? kotlin.reflect.KClass<*>)?.simpleName ?: "Any"
                if (isNullable) "$simpleName?" else simpleName
            }
        }
    }

}

class ProjectDelegateAccessor(private val variableName: String) {

    @Suppress("UNCHECKED_CAST")
    operator fun <T> getValue(thisRef: Any?, property: KProperty<*>): T {
        return ProjectProperty(variableName) as T
    }

}

val DslContainer.project: ProjectDelegateProvider
    get() = ProjectDelegateProvider(this)