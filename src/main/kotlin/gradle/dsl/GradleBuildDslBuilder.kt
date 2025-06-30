package gradle.dsl

import gradle.dsl.core.AutoRegisterContext
import gradle.dsl.core.DslElement
import gradle.dsl.core.PropertyAssignment
import gradle.dsl.plugins.embedded.GradlePluginExtensionBlock
import gradle.dsl.plugins.embedded.JavaExtensionBlock
import gradle.dsl.plugins.embedded.PluginsDependenciesSpecScopeBlock
import gradle.dsl.plugins.embedded.ProjectBlock
import gradle.dsl.plugins.embedded.PublishingExtensionBlock
import gradle.dsl.plugins.embedded.PublishingProxy
import gradle.dsl.plugins.embedded.ReportingExtensionBlock
import gradle.dsl.plugins.embedded.RepositoryHandlerBlock
import gradle.dsl.plugins.embedded.SigningExtensionBlock
import gradle.dsl.plugins.embedded.TasksBlock
import gradle.dsl.plugins.embedded.TestingExtensionBlock
import gradle.dsl.plugins.kotlin.KotlinExtensionBlock

class GradleBuildDslBuilder : AbstractScriptBuilder("build.gradle.kts") {

    private val root: ProjectBlock = ProjectBlock(parent = null)

    var group: String
        get() = throw UnsupportedOperationException("group is write-only in DSL context")
        set(value) {
            elements.add(PropertyAssignment("group", value))
        }

    var version: String
        get() = throw UnsupportedOperationException("version is write-only in DSL context")
        set(value) {
            elements.add(PropertyAssignment("version", value))
        }

    fun plugins(block: PluginsDependenciesSpecScopeBlock.() -> Unit = {}) = apply {
        elements.add(PluginsDependenciesSpecScopeBlock(root).apply(block))
    }

    fun subprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        elements.add(ProjectBlock("subprojects", root).apply(block))
    }

    fun allprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        elements.add(ProjectBlock("allprojects", root).apply(block))
    }

    fun tasks(block: TasksBlock.() -> Unit = {}) = apply {
        elements.add(TasksBlock(root, object : AutoRegisterContext {
            override fun autoRegister(element: DslElement) {
                elements.add(element)
            }
        }).apply(block))
    }

    fun repositories(block: RepositoryHandlerBlock.() -> Unit = {}) = apply {
        elements.add(RepositoryHandlerBlock(root).apply(block))
    }

    fun java(block: JavaExtensionBlock.() -> Unit = {}) = apply {
        elements.add(JavaExtensionBlock(root).apply(block))
    }

    fun testing(block: TestingExtensionBlock.() -> Unit = {}) = apply {
        elements.add(TestingExtensionBlock(root).apply(block))
    }

    fun gradlePlugin(block: GradlePluginExtensionBlock.() -> Unit = {}) = apply {
        elements.add(GradlePluginExtensionBlock(root).apply(block))
    }

    fun reporting(block: ReportingExtensionBlock.() -> Unit = {}) = apply {
        elements.add(ReportingExtensionBlock(root).apply(block))
    }

    val publishing: PublishingProxy = PublishingProxy(root, object : AutoRegisterContext {
        override fun autoRegister(element: DslElement) {
            elements.add(element)
        }
    })

    fun publishing(block: PublishingExtensionBlock.() -> Unit = {}) = apply {
        elements.add(PublishingExtensionBlock(root, object : AutoRegisterContext {
            override fun autoRegister(element: DslElement) {
                elements.add(element)
            }
        }).apply(block))
    }

    fun signing(block: SigningExtensionBlock.() -> Unit = {}) = apply {
        elements.add(SigningExtensionBlock(root).apply(block))
    }

    // ===== external

    fun kotlin(block: KotlinExtensionBlock.() -> Unit = {}) = apply {
        elements.add(KotlinExtensionBlock(root).apply(block))
    }

}