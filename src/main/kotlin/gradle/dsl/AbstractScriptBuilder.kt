package gradle.dsl

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.buildCodeBlock
import gradle.dsl.core.DslElement

abstract class AbstractScriptBuilder(
    private val scriptFileName: String,
    protected val elements: MutableList<DslElement> = mutableListOf()
) {

    fun buildFile(): FileSpec = FileSpec.scriptBuilder(scriptFileName)
        .addCode(buildCodeBlock { elements.forEach { add("%L", it.toCodeBlock()) } })
        .build()

    fun buildString(): String = buildFile().toString()

}