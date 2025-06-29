package gradle.dsl

import gradle.dsl.core.AutoRegisterContext
import gradle.dsl.core.DslElement
import gradle.dsl.core.PropertyAssignment
import gradle.dsl.plugins.embedded.GradlePluginExtensionBlock
import gradle.dsl.plugins.embedded.JavaExtensionBlock
import gradle.dsl.plugins.embedded.KotlinExtensionBlock
import gradle.dsl.plugins.embedded.PluginsDependenciesSpecScopeBlock
import gradle.dsl.plugins.embedded.ProjectBlock
import gradle.dsl.plugins.embedded.PublishingExtensionBlock
import gradle.dsl.plugins.embedded.PublishingProxy
import gradle.dsl.plugins.embedded.ReportingExtensionBlock
import gradle.dsl.plugins.embedded.RepositoryHandlerBlock
import gradle.dsl.plugins.embedded.SigningExtensionBlock
import gradle.dsl.plugins.embedded.TasksBlock
import gradle.dsl.plugins.embedded.TestingExtensionBlock

class GradleBuildDslBuilder : AbstractScriptBuilder("build.gradle.kts") {

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
        elements.add(PluginsDependenciesSpecScopeBlock().apply(block))
    }

    fun subprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        elements.add(ProjectBlock("subprojects").apply(block))
    }

    fun allprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        elements.add(ProjectBlock("allprojects").apply(block))
    }

    fun tasks(block: TasksBlock.() -> Unit = {}) = apply {
        elements.add(TasksBlock(object : AutoRegisterContext {
            override fun autoRegister(element: DslElement) {
                elements.add(element)
            }
        }).apply(block))
    }

    fun repositories(block: RepositoryHandlerBlock.() -> Unit = {}) = apply {
        elements.add(RepositoryHandlerBlock().apply(block))
    }

    fun java(block: JavaExtensionBlock.() -> Unit = {}) = apply {
        elements.add(JavaExtensionBlock().apply(block))
    }

    fun kotlin(block: KotlinExtensionBlock.() -> Unit = {}) = apply {
        elements.add(KotlinExtensionBlock().apply(block))
    }

    fun testing(block: TestingExtensionBlock.() -> Unit = {}) = apply {
        elements.add(TestingExtensionBlock().apply(block))
    }

    fun gradlePlugin(block: GradlePluginExtensionBlock.() -> Unit = {}) = apply {
        elements.add(GradlePluginExtensionBlock().apply(block))
    }

    fun reporting(block: ReportingExtensionBlock.() -> Unit = {}) = apply {
        elements.add(ReportingExtensionBlock().apply(block))
    }

    val publishing: PublishingProxy = PublishingProxy(object : AutoRegisterContext {
        override fun autoRegister(element: DslElement) {
            elements.add(element)
        }
    })

    fun publishing(block: PublishingExtensionBlock.() -> Unit = {}) = apply {
        elements.add(PublishingExtensionBlock(object : AutoRegisterContext {
            override fun autoRegister(element: DslElement) {
                elements.add(element)
            }
        }).apply(block))
    }

    fun signing(block: SigningExtensionBlock.() -> Unit = {}) = apply {
        elements.add(SigningExtensionBlock().apply(block))
    }

}