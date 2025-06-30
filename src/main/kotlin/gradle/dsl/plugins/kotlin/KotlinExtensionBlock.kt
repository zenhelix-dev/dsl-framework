package gradle.dsl.plugins.kotlin

import gradle.dsl.core.ArgumentType
import gradle.dsl.core.DslBlock
import gradle.dsl.core.DslElement
import gradle.dsl.core.FunctionCall
import gradle.dsl.core.Import
import gradle.dsl.core.PropertyAssignment
import gradle.dsl.core.TypedArgument

class KotlinExtensionBlock(parent: DslElement) : DslBlock("kotlin", parent) {

    fun explicitApi() = apply {
        addChild(FunctionCall("explicitApi"))
    }

    fun compilerOptions(block: KotlinCompilerOptionsBlock.() -> Unit) = apply {
        addChild(KotlinCompilerOptionsBlock(this).apply(block))
    }

}

class KotlinCompilerOptionsBlock(parent: DslElement) : DslBlock("compilerOptions", parent) {

    var jvmTarget: JvmTarget
        get() = throw UnsupportedOperationException("jvmTarget is write-only in DSL context")
        set(value) {
            findImportAware()?.addImport(Import("gradle.dsl.plugins.kotlin.JvmTarget"))

            addChild(PropertyAssignment("jvmTarget", TypedArgument("${JvmTarget::class.simpleName}.${value.name}", ArgumentType.CODE)))
        }

}