package core.generation

import core.ast.Node

/**
 * Интерфейс генератора кода
 */
interface CodeGenerator {
    fun generate(node: Node, options: GenerationOptions = GenerationOptions()): String
}

/**
 * Опции генерации кода
 */
data class GenerationOptions(
    val indentString: String = "    ",
    val lineEnding: String = "\n",
    val includeComments: Boolean = true,
    val includeSourceMap: Boolean = false
)

/**
 * Базовая реализация генератора кода
 */
abstract class BaseCodeGenerator : CodeGenerator {

    override fun generate(node: Node, options: GenerationOptions): String {
        val context = GenerationContext(options)
        generateNode(node, context)
        return context.getOutput()
    }

    abstract fun generateNode(node: Node, context: GenerationContext)
}

/**
 * Контекст генерации кода
 */
class GenerationContext(
    private val options: GenerationOptions
) {
    private val output = StringBuilder()
    private var indentLevel = 0

    fun append(text: String): GenerationContext {
        output.append(text)
        return this
    }

    fun appendLine(text: String = ""): GenerationContext {
        if (text.isNotEmpty()) {
            output.append(getIndent()).append(text)
        }
        output.append(options.lineEnding)
        return this
    }

    fun indent(): GenerationContext {
        indentLevel++
        return this
    }

    fun dedent(): GenerationContext {
        indentLevel--
        return this
    }

    fun getIndent(): String {
        return options.indentString.repeat(indentLevel)
    }

    fun getOutput(): String = output.toString()
}