package dsl.domains.gradle.generation

import core.ast.Node
import core.generation.BaseCodeGenerator
import core.generation.GenerationContext
import dsl.domains.gradle.nodes.CreatePublicationNode
import dsl.domains.gradle.nodes.FromComponentNode
import dsl.domains.gradle.nodes.GradleBlockNode
import dsl.domains.gradle.nodes.MavenLocalNode
import dsl.domains.gradle.nodes.MavenNode
import dsl.domains.gradle.nodes.PublicationNode
import dsl.domains.gradle.nodes.PublicationsNode
import dsl.domains.gradle.nodes.PublishingNode
import dsl.domains.gradle.nodes.RepositoriesNode
import dsl.domains.gradle.nodes.SignPublicationsNode
import dsl.domains.gradle.nodes.SigningNode
import dsl.domains.gradle.nodes.UseInMemoryPgpKeysNode
import languages.kotlin.generation.KotlinCodeGenerator

/**
 * Генератор кода для Gradle DSL
 */
class GradleCodeGenerator : BaseCodeGenerator() {

    private val kotlinGenerator = KotlinCodeGenerator()

    override fun generateNode(node: Node, context: GenerationContext) {
        when (node) {
            is PublishingNode -> generatePublishing(node, context)
            is SigningNode -> generateSigning(node, context)
            is RepositoriesNode -> generateRepositories(node, context)
            is PublicationsNode -> generatePublications(node, context)
            is CreatePublicationNode -> generateCreatePublication(node, context)
            is PublicationNode -> generatePublication(node, context)
            is MavenLocalNode -> context.appendLine("mavenLocal()")
            is MavenNode -> context.appendLine("maven(\"${node.url}\")")
            is FromComponentNode -> context.appendLine("from(components[\"${node.component.name}\"])")
            is UseInMemoryPgpKeysNode -> context.appendLine("useInMemoryPgpKeys(\"${node.keyId}\", \"${node.password}\")")
            is SignPublicationsNode -> context.appendLine("sign(${node.publications.toPath()})")
            is GradleBlockNode -> generateBlock(node, context)
            else -> kotlinGenerator.generateNode(node, context)
        }
    }

    private fun generatePublishing(node: PublishingNode, context: GenerationContext) {
        context.appendLine("publishing {")
        context.indent()
        generateNode(node.repositories, context)
        generateNode(node.publications, context)
        context.dedent()
        context.appendLine("}")
    }

    private fun generateSigning(node: SigningNode, context: GenerationContext) {
        context.appendLine("signing {")
        context.indent()
        node.configurations.forEach { config ->
            generateNode(config, context)
        }
        context.dedent()
        context.appendLine("}")
    }

    private fun generateRepositories(node: RepositoriesNode, context: GenerationContext) {
        context.appendLine("repositories {")
        context.indent()
        node.repositories.forEach { repo ->
            generateNode(repo, context)
        }
        context.dedent()
        context.appendLine("}")
    }

    private fun generatePublications(node: PublicationsNode, context: GenerationContext) {
        context.appendLine("publications {")
        context.indent()
        node.publications.forEach { pub ->
            generateNode(pub, context)
        }
        context.dedent()
        context.appendLine("}")
    }

    private fun generateCreatePublication(node: CreatePublicationNode, context: GenerationContext) {
        context.appendLine("create<${node.type}>(\"${node.name}\") {")
        context.indent()
        generateNode(node.configuration, context)
        context.dedent()
        context.appendLine("}")
    }

    private fun generatePublication(node: PublicationNode, context: GenerationContext) {
        node.configurations.forEach { config ->
            generateNode(config, context)
        }
    }

    private fun generateBlock(node: GradleBlockNode, context: GenerationContext) {
        node.statements.forEach { statement ->
            generateNode(statement, context)
        }
    }
}