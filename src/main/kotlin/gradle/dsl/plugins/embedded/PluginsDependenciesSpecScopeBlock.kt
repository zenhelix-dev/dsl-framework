package gradle.dsl.plugins.embedded

import com.squareup.kotlinpoet.CodeBlock
import gradle.dsl.core.DslBlock
import gradle.dsl.core.DslContainer
import gradle.dsl.core.DslElement
import gradle.dsl.core.VariableElement

@Suppress("PropertyName")
class PluginsDependenciesSpecScopeBlock(parent: DslElement) : DslBlock("plugins", parent) {

    fun id(pluginId: String): PluginDependencySpec {
        return PluginDependencySpec(pluginId, this)
    }

    val `kotlin-dsl`: Unit by lazy { addChild(VariableElement("`kotlin-dsl`")) }
    val `jacoco-report-aggregation`: Unit by lazy { addChild(VariableElement("`jacoco-report-aggregation`")) }
    val `jvm-test-suite`: Unit by lazy { addChild(VariableElement("`jvm-test-suite`")) }
    val `maven-publish`: Unit by lazy { addChild(VariableElement("`maven-publish`")) }
    val `java-library`: Unit by lazy { addChild(VariableElement("`java-library`")) }

    val signing: Unit by lazy { addChild(VariableElement("signing")) }
    val java: Unit by lazy { addChild(VariableElement("java")) }
    val kotlin: Unit by lazy { addChild(VariableElement("kotlin")) }
    val application: Unit by lazy { addChild(VariableElement("application")) }

}

class PluginDependencySpec(
    private val pluginId: String,
    private val container: DslContainer
) {
    private var hasVersion = false

    init {
        container.addChild(PluginDependencyCall(pluginId, null))
    }

    infix fun version(version: String) {
        if (!hasVersion) {
            container.children.removeLastOrNull()
            container.addChild(PluginDependencyCall(pluginId, version))
            hasVersion = true
        }
    }
}

class PluginDependencyCall(
    private val pluginId: String,
    private val version: String? = null
) : DslElement {

    override fun toCodeBlock(): CodeBlock {
        return if (version != null) {
            CodeBlock.of("id(%S) version %S\n", pluginId, version)
        } else {
            CodeBlock.of("id(%S)\n", pluginId)
        }
    }
}