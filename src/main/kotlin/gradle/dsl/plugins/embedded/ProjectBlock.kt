package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock
import gradle.dsl.core.DslElement
import gradle.dsl.core.Import
import gradle.dsl.core.ImportAware
import gradle.dsl.core.PropertyAssignment
import gradle.dsl.plugins.kotlin.KotlinExtensionBlock

class ProjectBlock(
    override val blockName: String = "",
    parent: DslElement?,
    private val importAware: ImportAware? = null
) : DslBlock(blockName, parent), ImportAware {

    override fun addImport(import: Import) {
        importAware?.addImport(import)
    }

    var group: String
        get() = throw UnsupportedOperationException("group is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("group", value))
        }

    var version: String
        get() = throw UnsupportedOperationException("version is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("version", value))
        }

    fun plugins(block: PluginsDependenciesSpecScopeBlock.() -> Unit = {}) = apply {
        addChild(PluginsDependenciesSpecScopeBlock(this).apply(block))
    }

    fun subprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        addChild(ProjectBlock("subprojects", this).apply(block))
    }

    fun allprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        addChild(ProjectBlock("allprojects", this).apply(block))
    }

    fun tasks(block: TasksBlock.() -> Unit = {}) = apply {
        addChild(TasksBlock(this).apply(block))
    }

    fun repositories(block: RepositoryHandlerBlock.() -> Unit = {}) = apply {
        addChild(RepositoryHandlerBlock(this).apply(block))
    }

    val publishing: PublishingProxy = PublishingProxy(this, this)

    fun publishing(block: PublishingExtensionBlock.() -> Unit = {}) = apply {
        addChild(PublishingExtensionBlock(this).apply(block))
    }

    fun signing(block: SigningExtensionBlock.() -> Unit = {}) = apply {
        addChild(SigningExtensionBlock(this).apply(block))
    }

    fun java(block: JavaExtensionBlock.() -> Unit = {}) = apply {
        addChild(JavaExtensionBlock(this).apply(block))
    }

    fun testing(block: TestingExtensionBlock.() -> Unit = {}) = apply {
        addChild(TestingExtensionBlock(this).apply(block))
    }

    fun gradlePlugin(block: GradlePluginExtensionBlock.() -> Unit = {}) = apply {
        addChild(GradlePluginExtensionBlock(this).apply(block))
    }

    fun reporting(block: ReportingExtensionBlock.() -> Unit = {}) = apply {
        addChild(ReportingExtensionBlock(this).apply(block))
    }

    // ===== external

    fun kotlin(block: KotlinExtensionBlock.() -> Unit = {}) = apply {
        addChild(KotlinExtensionBlock(this).apply(block))
    }


}