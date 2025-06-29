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
    val children: MutableList<DslElement>

    fun addChild(element: DslElement) {
        children.add(element)
    }
}

abstract class DslBlock(
    open val blockName: String,
    override val children: MutableList<DslElement> = mutableListOf()
) : DslContainer, AutoRegisterContext {

    override fun toCodeBlock(): CodeBlock = buildCodeBlock {
        beginControlFlow(blockName)
        withIndent {
            children.forEach { add("%L", it.toCodeBlock()) }
        }
        endControlFlow()
    }

    override fun autoRegister(element: DslElement) {
        addChild(element)
    }
}

abstract class DslBodyBlock(
    blockName: String,
    override val children: MutableList<DslElement> = mutableListOf()
) : DslBlock(blockName, children) {

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