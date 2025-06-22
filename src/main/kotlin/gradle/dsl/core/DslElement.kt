package gradle.dsl.core

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
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

class FunctionCall(
    private val name: String,
    private val arguments: List<Any> = emptyList()
) : DslElement {

    override fun toCodeBlock(): CodeBlock = FunSpec.builder("")
        .addStatement("$name(%L)", *arguments.toTypedArray())
        .build()
        .body

}

data class PropertyAssignment(
    val name: String, val value: Any
) : DslElement {
    override fun toCodeBlock(): CodeBlock {
        return CodeBlock.of("%L = %L\n", name, value)
    }
}