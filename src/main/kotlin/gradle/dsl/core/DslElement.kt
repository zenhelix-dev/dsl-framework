package gradle.dsl.core

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent
import gradle.dsl.core.FormattingHelper.formatArgument
import gradle.dsl.core.FormattingHelper.formatArguments

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

    fun declareVal(name: String, value: Any, type: String? = null, nullable: Boolean = false): VariableReference {
        val declaration = VariableDeclaration(name, value, type, nullable, false)
        addChild(declaration)
        return VariableReference(name)
    }

    fun declareVar(name: String, value: Any, type: String? = null, nullable: Boolean = false): VariableReference {
        val declaration = VariableDeclaration(name, value, type, nullable, true)
        addChild(declaration)
        return VariableReference(name)
    }

    fun variable(
        name: String,
        value: Any,
        type: String? = null,
        nullable: Boolean = false,
        mutable: Boolean = false
    ): VariableDelegate {
        val declaration = VariableDeclaration(name, value, type, nullable, mutable)
        addChild(declaration)
        return VariableDelegate(VariableReference(name))
    }

    fun valOf(name: String, value: Any, type: String? = null, nullable: Boolean = false): VariableDelegate {
        return variable(name, value, type, nullable, false)
    }

    fun varOf(name: String, value: Any, type: String? = null, nullable: Boolean = false): VariableDelegate {
        return variable(name, value, type, nullable, true)
    }

    class VariableDelegate(private val reference: VariableReference) {
        operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): VariableReference {
            return reference
        }
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
