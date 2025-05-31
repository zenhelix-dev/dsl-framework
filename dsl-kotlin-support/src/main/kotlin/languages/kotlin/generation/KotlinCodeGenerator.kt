package languages.kotlin.generation

import core.ast.BlockNode
import core.ast.LiteralNode
import core.ast.LiteralType
import core.ast.Node
import core.ast.ReferenceNode
import core.context.Modifier
import core.generation.BaseCodeGenerator
import core.generation.GenerationContext
import languages.kotlin.ast.KotlinCallNode
import languages.kotlin.ast.KotlinClassNode
import languages.kotlin.ast.KotlinFunctionNode
import languages.kotlin.ast.KotlinIndexAccessNode
import languages.kotlin.ast.KotlinLambdaNode
import languages.kotlin.ast.KotlinParameterNode
import languages.kotlin.ast.KotlinPropertyNode

/**
 * Генератор кода для Kotlin
 */
class KotlinCodeGenerator : BaseCodeGenerator() {

    override fun generateNode(node: Node, context: GenerationContext) {
        when (node) {
            is KotlinClassNode -> generateClass(node, context)
            is KotlinFunctionNode -> generateFunction(node, context)
            is KotlinPropertyNode -> generateProperty(node, context)
            is KotlinCallNode -> generateCall(node, context)
            is KotlinLambdaNode -> generateLambda(node, context)
            is KotlinIndexAccessNode -> generateIndexAccess(node, context)
            is BlockNode -> generateBlock(node, context)
            is LiteralNode -> generateLiteral(node, context)
            is ReferenceNode -> generateReference(node, context)
            else -> throw UnsupportedOperationException("Cannot generate code for ${node::class}")
        }
    }

    private fun generateClass(node: KotlinClassNode, context: GenerationContext) {
        generateModifiers(node.modifiers, context)
        context.append("class ${node.name}")

        if (node.typeParameters.isNotEmpty()) {
            context.append("<")
            node.typeParameters.forEachIndexed { index, param ->
                if (index > 0) context.append(", ")
                context.append(param.name)
                if (param.constraints.isNotEmpty()) {
                    context.append(" : ")
                    param.constraints.forEachIndexed { i, constraint ->
                        if (i > 0) context.append(" & ")
                        generateNode(constraint, context)
                    }
                }
            }
            context.append(">")
        }

        if (node.superTypes.isNotEmpty()) {
            context.append(" : ")
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

    private fun generateFunction(node: KotlinFunctionNode, context: GenerationContext) {
        generateModifiers(node.modifiers, context)
        context.append("fun ")

        if (node.typeParameters.isNotEmpty()) {
            context.append("<")
            node.typeParameters.forEachIndexed { index, param ->
                if (index > 0) context.append(", ")
                context.append(param.name)
            }
            context.append("> ")
        }

        context.append("${node.name}(")

        node.parameters.forEachIndexed { index, param ->
            if (index > 0) context.append(", ")
            generateParameter(param, context)
        }

        context.append(")")

        node.returnType?.let { returnType ->
            context.append(": ")
            generateNode(returnType, context)
        }

        node.body?.let { body ->
            context.append(" ")
            generateBlock(body, context)
        }
    }

    private fun generateProperty(node: KotlinPropertyNode, context: GenerationContext) {
        generateModifiers(node.modifiers, context)
        context.append(if (Modifier.VAR in node.modifiers) "var " else "val ")
        context.append(node.name)

        node.type?.let { type ->
            context.append(": ")
            generateNode(type, context)
        }

        node.initializer?.let { init ->
            context.append(" = ")
            generateNode(init, context)
        }

        if (node.getter != null || node.setter != null) {
            context.appendLine()
            context.indent()

            node.getter?.let { getter ->
                context.append("get()")
                getter.body?.let { body ->
                    context.append(" ")
                    generateBlock(body, context)
                }
                context.appendLine()
            }

            node.setter?.let { setter ->
                context.append("set(value)")
                setter.body?.let { body ->
                    context.append(" ")
                    generateBlock(body, context)
                }
                context.appendLine()
            }

            context.dedent()
        }
    }

    private fun generateCall(node: KotlinCallNode, context: GenerationContext) {
        node.receiver?.let { receiver ->
            generateNode(receiver, context)
            context.append(".")
        }

        context.append(node.name)

        if (node.typeArguments.isNotEmpty()) {
            context.append("<")
            node.typeArguments.forEachIndexed { index, typeArg ->
                if (index > 0) context.append(", ")
                generateNode(typeArg, context)
            }
            context.append(">")
        }

        context.append("(")
        node.arguments.forEachIndexed { index, arg ->
            if (index > 0) context.append(", ")
            generateNode(arg, context)
        }
        context.append(")")

        node.lambdaArgument?.let { lambda ->
            context.append(" ")
            generateLambda(lambda, context)
        }
    }

    private fun generateLambda(node: KotlinLambdaNode, context: GenerationContext) {
        context.append("{")
        if (node.parameters.isNotEmpty()) {
            context.append(" ")
            node.parameters.forEachIndexed { index, param ->
                if (index > 0) context.append(", ")
                context.append(param.name)
                context.append(": ")
                generateNode(param.type, context)
            }
            context.append(" ->")
        }

        context.appendLine()
        context.indent()
        generateNode(node.body, context)
        context.dedent()
        context.append("}")
    }

    private fun generateIndexAccess(node: KotlinIndexAccessNode, context: GenerationContext) {
        generateNode(node.receiver, context)
        context.append("[")
        node.indices.forEachIndexed { index, expr ->
            if (index > 0) context.append(", ")
            generateNode(expr, context)
        }
        context.append("]")
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

    private fun generateParameter(node: KotlinParameterNode, context: GenerationContext) {
        generateModifiers(node.modifiers, context)
        context.append("${node.name}: ")
        generateNode(node.type, context)

        node.defaultValue?.let { default ->
            context.append(" = ")
            generateNode(default, context)
        }
    }

    private fun generateLiteral(node: LiteralNode, context: GenerationContext) {
        when (node.literalType) {
            LiteralType.STRING -> context.append("\"${node.value}\"")
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
        val sortedModifiers = modifiers.sortedBy { modifierOrder(it) }
        sortedModifiers.forEach { modifier ->
            context.append("${modifier.name.lowercase()} ")
        }
    }

    private fun modifierOrder(modifier: Modifier): Int {
        return when (modifier) {
            Modifier.PUBLIC -> 0
            Modifier.PRIVATE -> 1
            Modifier.PROTECTED -> 2
            Modifier.INTERNAL -> 3
            Modifier.ABSTRACT -> 4
            Modifier.FINAL -> 5
            Modifier.OPEN -> 6
            Modifier.SEALED -> 7
            Modifier.INLINE -> 8
            Modifier.SUSPEND -> 9
            Modifier.OPERATOR -> 10
            else -> 11
        }
    }
}