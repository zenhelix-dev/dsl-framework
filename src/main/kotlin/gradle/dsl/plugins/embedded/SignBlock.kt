package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock
import gradle.dsl.core.DslElement
import gradle.dsl.core.FunctionCall
import gradle.dsl.core.codeValue

class SigningExtensionBlock(parent: DslElement) : DslBlock("signing", parent) {

    fun useInMemoryPgpKeys(keyId: String?, key: String?, password: String?) = apply {
        addChild(FunctionCall("useInMemoryPgpKeys", listOf(keyId ?: codeValue("A"), key ?: codeValue("B"), password ?: codeValue("C"))))
    }

    fun useInMemoryPgpKeys(key: String?, password: String?) = apply {
        addChild(FunctionCall("useInMemoryPgpKeys", listOf(key ?: codeValue("B"), password ?: codeValue("C"))))
    }

    fun sign(target: PublicationsContainerBlock) = apply {
        addChild(FunctionCall("sign", listOf(target)))
    }

}