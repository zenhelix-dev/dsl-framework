package gradle.dsl.core

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent

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
) : DslContainer {

    override fun toCodeBlock(): CodeBlock = buildCodeBlock {
        beginControlFlow(blockName)
        withIndent {
            children.forEach { add("%L", it.toCodeBlock()) }
        }
        endControlFlow()
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
        val parametersFormatString = if (arguments.isNotEmpty()) {
            buildString {
                repeat(arguments.size) { index ->
                    if (index > 0) append(", ")
                    append("%S")
                }
            }
        } else {
            ""
        }

        val builder = CodeBlock.builder()

        if (body != null) {
            builder
                .beginControlFlow("$name($parametersFormatString)", *arguments.toTypedArray())
                .add(body.toCodeBlock())
                .endControlFlow()
        } else {
            builder.add("$name($parametersFormatString)\n", *arguments.toTypedArray())
        }

        return builder.build()
    }

}

data class PropertyAssignment(val name: String, val value: Any) : DslElement {
    override fun toCodeBlock(): CodeBlock = CodeBlock.of("%L = %L\n", name, value)
}