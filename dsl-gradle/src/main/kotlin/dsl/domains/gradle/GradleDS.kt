package dsl.domains.gradle

import core.context.Context
import dsl.domains.gradle.builders.GradleRootBuilder
import dsl.domains.gradle.generation.GradleCodeGenerator
import dsl.domains.gradle.nodes.GradleNode
import dsl.framework.BaseDSL

/**
 * Gradle DSL для создания build скриптов
 */
class GradleDSL(context: Context) :
    BaseDSL<GradleNode, GradleRootBuilder>(context) {

    override fun createBuilder(): GradleRootBuilder {
        return GradleRootBuilder(context)
    }

    override fun execute(node: GradleNode): Any? {
        return node
    }

    override fun generate(node: GradleNode): String {
        val generator = GradleCodeGenerator()
        return generator.generate(node)
    }

    operator fun invoke(block: GradleRootBuilder.() -> Unit): GradleNode {
        return buildWith(block)
    }
}