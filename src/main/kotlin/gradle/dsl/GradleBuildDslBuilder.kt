package gradle.dsl

import gradle.dsl.plugins.embedded.PluginsDependenciesSpecScopeBlock
import gradle.dsl.plugins.embedded.ProjectBlock

class GradleBuildDslBuilder : AbstractScriptBuilder("build.gradle.kts") {

    fun plugins(block: PluginsDependenciesSpecScopeBlock.() -> Unit = {}) = apply {
        elements.add(PluginsDependenciesSpecScopeBlock().apply(block))
    }

    fun subprojects(block: ProjectBlock.() -> Unit = {}) = apply {
        elements.add(ProjectBlock("subprojects").apply(block))
    }

}
