package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock
import gradle.dsl.core.DslElement
import gradle.dsl.core.FunctionCall
import gradle.dsl.core.VariableElement

@Suppress("PropertyName")
class PluginsDependenciesSpecScopeBlock(parent: DslElement) : DslBlock("plugins", parent) {

    fun id(pluginId: String) = apply {
        addChild(FunctionCall("id", listOf(pluginId)))
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