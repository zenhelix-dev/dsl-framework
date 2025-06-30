package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock
import gradle.dsl.core.DslElement
import gradle.dsl.core.FunctionCall

class SigningExtensionBlock(parent: DslElement) : DslBlock("signing", parent) {

    fun useInMemoryPgpKeys(keyId: String, key: String, password: String) = apply {
        addChild(FunctionCall("useInMemoryPgpKeys", listOf(keyId, key, password)))
    }

    fun useInMemoryPgpKeys(key: String, password: String) = apply {
        addChild(FunctionCall("useInMemoryPgpKeys", listOf(key, password)))
    }

    fun sign(target: PublicationsContainerBlock) = apply {
        addChild(FunctionCall("sign", listOf(target)))
    }

}