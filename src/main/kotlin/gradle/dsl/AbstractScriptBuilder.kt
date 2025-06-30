package gradle.dsl

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.Import
import com.squareup.kotlinpoet.buildCodeBlock
import gradle.dsl.core.DslElement

abstract class AbstractScriptBuilder(
    private val scriptFileName: String,
    protected val elements: MutableList<DslElement> = mutableListOf(),
    protected val imports: MutableSet<Import> = mutableSetOf()
) {

    fun addImport(import: Import) {
        imports.add(import)
    }

    fun buildFile(): FileSpec = FileSpec.scriptBuilder(scriptFileName)
        .apply { this@AbstractScriptBuilder.imports.onEach { addImport(it) } }
        .addCode(buildCodeBlock { elements.forEach { add("%L", it.toCodeBlock()) } })
        .build()

    fun buildString(): String = buildFile().toString()

}