package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock
import gradle.dsl.core.FunctionCall

class PluginsDslBlock : DslBlock("plugins") {

    fun id(pluginId: String) = apply {
        addChild(FunctionCall("id", listOf(pluginId)))
    }

}