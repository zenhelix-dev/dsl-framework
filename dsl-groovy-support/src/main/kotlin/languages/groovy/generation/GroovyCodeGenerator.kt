package languages.groovy.generation

import core.ast.BlockNode
import core.ast.LiteralNode
import core.ast.LiteralType
import core.ast.Node
import core.ast.ReferenceNode
import core.context.Modifier
import core.generation.BaseCodeGenerator
import core.generation.GenerationContext
import languages.groovy.ast.GroovyClassNode
import languages.groovy.ast.GroovyClosureNode
import languages.groovy.ast.GroovyFieldNode
import languages.groovy.ast.GroovyMethodCallNode
import languages.groovy.ast.GroovyMethodNode
import languages.groovy.ast.GroovyParameterNode
import languages.groovy.ast.GroovyPropertyAccessNode
import languages.groovy.ast.GroovyStringInterpolationNode
import languages.groovy.ast.StringPart

/**
 * Генератор кода для Groovy
 */
class GroovyCodeGenerator : BaseCodeGenerator() {

    override fun generateNode(node: Node, context: GenerationContext) {
        when (node) {
            is GroovyClassNode -> generateClass(node, context)
            is GroovyMethodNode -> generateMethod(node, context)
            is GroovyFieldNode -> generateField(node, context)
            is GroovyMethodCallNode -> generateMethodCall(node, context)
            is GroovyClosureNode -> generateClosure(node, context)
            is GroovyPropertyAccessNode -> generatePropertyAccess(node, context)
            is GroovyStringInterpolationNode -> generateStringInterpolation(node, context)
            is BlockNode -> generateBlock(node, context)
            is LiteralNode -> generateLiteral(node, context)
            is ReferenceNode -> generateReference(node, context)
            else -> throw UnsupportedOperationException("Cannot generate code for ${node::class}")
        }
    }

    private fun generateClass(node: GroovyClassNode, context: GenerationContext) {
        generateModifiers(node.modifiers, context)
        context.append("class ${node.name}")

        if (node.superTypes.isNotEmpty()) {
            context.append(" extends ")
            node.superTypes.forEachIndexed { index, superType ->
                if (index > 0) context.append(", ")
                generateNode(superType, context)
            }
        }

        context.appendLine(" {")
        context.indent()

        node.members.forEach { member ->
            generateNode(member, context)
            context.appendLine()
        }

        context.dedent()
        context.appendLine("}")
    }

    private fun generateMethod(node: GroovyMethodNode, context: GenerationContext) {
        generateModifiers(node.modifiers, context)

        node.returnType?.let { returnType ->
            generateNode(returnType, context)
            context.append(" ")
        }

        context.append("${node.name}(")

        node.parameters.forEachIndexed { index, param ->
            if (index > 0) context.append(", ")
            generateParameter(param, context)
        }

        context.append(")")

        node.body?.let { body ->
            context.append(" ")
            generateBlock(body, context)
        }
    }

    private fun generateField(node: GroovyFieldNode, context: GenerationContext) {
        generateModifiers(node.modifiers, context)

        node.type?.let { type ->
            generateNode(type, context)
            context.append(" ")
        }

        context.append(node.name)

        node.initializer?.let { init ->
            context.append(" = ")
            generateNode(init, context)
        }
    }

    private fun generateMethodCall(node: GroovyMethodCallNode, context: GenerationContext) {
        node.receiver?.let { receiver ->
            generateNode(receiver, context)
            context.append(".")
        }

        context.append(node.name)

        if (node.arguments.isNotEmpty()) {
            context.append("(")
            node.arguments.forEachIndexed { index, arg ->
                if (index > 0) context.append(", ")
                generateNode(arg, context)
            }
            context.append(")")
        }

        node.closureArgument?.let { closure ->
            context.append(" ")
            generateClosure(closure, context)
        }
    }

    private fun generateClosure(node: GroovyClosureNode, context: GenerationContext) {
        context.append("{")

        if (node.parameters.isNotEmpty()) {
            context.append(" ")
            node.parameters.forEachIndexed { index, param ->
                if (index > 0) context.append(", ")
                context.append(param.name)
                param.type?.let { type ->
                    context.append(": ")
                    generateNode(type, context)
                }
            }
            context.append(" ->")
        }

        context.appendLine()
        context.indent()
        generateNode(node.body, context)
        context.dedent()
        context.append("}")
    }

    private fun generatePropertyAccess(node: GroovyPropertyAccessNode, context: GenerationContext) {
        generateNode(node.receiver, context)
        context.append(".${node.propertyName}")
    }

    private fun generateStringInterpolation(node: GroovyStringInterpolationNode, context: GenerationContext) {
        context.append("\"")
        node.parts.forEach { part ->
            when (part) {
                is StringPart.Text -> context.append(part.value)
                is StringPart.Expression -> {
                    context.append("\${")
                    generateNode(part.expression, context)
                    context.append("}")
                }
            }
        }
        context.append("\"")
    }

    private fun generateBlock(node: BlockNode, context: GenerationContext) {
        context.appendLine("{")
        context.indent()

        node.statements.forEach { statement ->
            generateNode(statement, context)
            context.appendLine()
        }

        context.dedent()
        context.append("}")
    }

    private fun generateParameter(node: GroovyParameterNode, context: GenerationContext) {
        node.type?.let { type ->
            generateNode(type, context)
            context.append(" ")
        }

        context.append(node.name)

        node.defaultValue?.let { default ->
            context.append(" = ")
            generateNode(default, context)
        }
    }

    private fun generateLiteral(node: LiteralNode, context: GenerationContext) {
        when (node.literalType) {
            LiteralType.STRING -> context.append("'${node.value}'")
            LiteralType.NUMBER -> context.append(node.value.toString())
            LiteralType.BOOLEAN -> context.append(node.value.toString())
            LiteralType.NULL -> context.append("null")
        }
    }

    private fun generateReference(node: ReferenceNode, context: GenerationContext) {
        node.qualifier?.let { qualifier ->
            generateReference(qualifier, context)
            context.append(".")
        }
        context.append(node.name)
    }

    private fun generateModifiers(modifiers: Set<Modifier>, context: GenerationContext) {
        val groovyModifiers = modifiers.filter { isGroovyModifier(it) }
            .sortedBy { modifierOrder(it) }

        groovyModifiers.forEach { modifier ->
            context.append("${groovyModifierName(modifier)} ")
        }
    }

    private fun isGroovyModifier(modifier: Modifier): Boolean {
        return when (modifier) {
            Modifier.PUBLIC, Modifier.PRIVATE, Modifier.PROTECTED,
            Modifier.ABSTRACT, Modifier.FINAL -> true

            else -> false
        }
    }

    private fun groovyModifierName(modifier: Modifier): String {
        return when (modifier) {
            Modifier.PUBLIC -> "public"
            Modifier.PRIVATE -> "private"
            Modifier.PROTECTED -> "protected"
            Modifier.ABSTRACT -> "abstract"
            Modifier.FINAL -> "final"
            else -> modifier.name.lowercase()
        }
    }

    private fun modifierOrder(modifier: Modifier): Int {
        return when (modifier) {
            Modifier.PUBLIC -> 0
            Modifier.PRIVATE -> 1
            Modifier.PROTECTED -> 2
            Modifier.ABSTRACT -> 3
            Modifier.FINAL -> 4
            else -> 5
        }
    }
}