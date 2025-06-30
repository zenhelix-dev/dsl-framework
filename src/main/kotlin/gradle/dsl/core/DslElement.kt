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
    private val arguments: List<Any> = emptyList(),
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