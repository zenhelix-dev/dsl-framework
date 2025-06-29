package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock
import gradle.dsl.core.FunctionCall

class KotlinExtensionBlock : DslBlock("kotlin") {

    fun explicitApi() = apply {
        addChild(FunctionCall("explicitApi"))
    }

    fun compilerOptions(block: KotlinCompilerOptionsBlock.() -> Unit) = apply {
        addChild(KotlinCompilerOptionsBlock().apply(block))
    }

}

class KotlinCompilerOptionsBlock : DslBlock("compilerOptions") {

}