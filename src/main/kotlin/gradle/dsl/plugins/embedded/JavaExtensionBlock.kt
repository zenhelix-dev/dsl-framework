package gradle.dsl.plugins.embedded

import gradle.dsl.core.ArgumentType
import gradle.dsl.core.DslBlock
import gradle.dsl.core.DslElement
import gradle.dsl.core.FunctionCall
import gradle.dsl.core.PropertyAssignment
import gradle.dsl.core.TypedArgument
import org.gradle.api.JavaVersion

class JavaExtensionBlock(parent: DslElement) : DslBlock("java", parent) {

    var sourceCompatibility: Any
        get() = throw UnsupportedOperationException("sourceCompatibility is write-only in DSL context")
        set(value) {
            val property = if (value is JavaVersion) {
                TypedArgument("${JavaVersion::class.simpleName}.${value.name}", ArgumentType.CODE)
            } else {
                value
            }
            addChild(PropertyAssignment("sourceCompatibility", property))
        }

    var targetCompatibility: Any
        get() = throw UnsupportedOperationException("targetCompatibility is write-only in DSL context")
        set(value) {
            val property = if (value is JavaVersion) {
                TypedArgument("${JavaVersion::class.simpleName}.${value.name}", ArgumentType.CODE)
            } else {
                value
            }
            addChild(PropertyAssignment("targetCompatibility", property))
        }

    fun withJavadocJar() = apply {
        addChild(FunctionCall("withJavadocJar"))
    }

    fun withSourcesJar() = apply {
        addChild(FunctionCall("withSourcesJar"))
    }

    fun toolchain(block: JavaToolchainBlock.() -> Unit) = apply {
        addChild(JavaToolchainBlock(this).apply(block))
    }

}

class JavaToolchainBlock(parent: DslElement) : DslBlock("toolchain", parent) {

}