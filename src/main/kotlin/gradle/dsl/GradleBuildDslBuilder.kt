package gradle.dsl

import gradle.dsl.plugins.embedded.PluginsDslBlock
import gradle.dsl.plugins.embedded.ProjectBlock

class GradleBuildDslBuilder : AbstractScriptBuilder("build.gradle.kts") {

    fun plugins(block: PluginsDslBlock.() -> Unit) = apply {
        elements.add(PluginsDslBlock().apply(block))
    }

    fun subprojects(block: ProjectBlock.() -> Unit) = apply {
        elements.add(ProjectBlock("subprojects").apply(block))
    }

}
