package gradle.dsl

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

    private val root: ProjectBlock = ProjectBlock(blockName = "", parent = null)

    init {
        elements.add(root)
    }

    var group: String
        get() = root.group
        set(value) {
            root.group = value
        }

    var version: String
        get() = root.version
        set(value) {
            root.version = value
        }

    fun plugins(block: PluginsDependenciesSpecScopeBlock.() -> Unit = {}) = apply {
        root.plugins(block)
    }

    fun subprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        root.subprojects(block)
    }

    fun allprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        root.allprojects(block)
    }

    fun tasks(block: TasksBlock.() -> Unit = {}) = apply {
        root.tasks(block)
    }

    fun repositories(block: RepositoryHandlerBlock.() -> Unit = {}) = apply {
        root.repositories(block)
    }

    fun java(block: JavaExtensionBlock.() -> Unit = {}) = apply {
        root.java(block)
    }

    fun testing(block: TestingExtensionBlock.() -> Unit = {}) = apply {
        root.testing(block)
    }

    fun gradlePlugin(block: GradlePluginExtensionBlock.() -> Unit = {}) = apply {
        root.gradlePlugin(block)
    }

    fun reporting(block: ReportingExtensionBlock.() -> Unit = {}) = apply {
        root.reporting(block)
    }

    val publishing: PublishingProxy = root.publishing

    fun publishing(block: PublishingExtensionBlock.() -> Unit = {}) = apply {
        root.publishing(block)
    }

    fun signing(block: SigningExtensionBlock.() -> Unit = {}) = apply {
        root.signing(block)
    }

    // ===== external

    fun kotlin(block: KotlinExtensionBlock.() -> Unit = {}) = apply {
        root.kotlin(block)
    }

}