package gradle.dsl

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.buildCodeBlock
import gradle.dsl.core.DslElement
import gradle.dsl.core.Import
import gradle.dsl.core.ImportAware

abstract class AbstractScriptBuilder(
    private val scriptFileName: String,
    protected val elements: MutableList<DslElement> = mutableListOf(),
    private val imports: MutableSet<Import> = mutableSetOf()
) : ImportAware {

    override fun addImport(import: Import) {
        imports.add(import)
    }

    fun buildFile(): FileSpec = FileSpec.scriptBuilder(scriptFileName)
        .apply { this@AbstractScriptBuilder.imports.onEach { this.addImport("", it.qualifiedName) } }
        .addCode(buildCodeBlock { elements.forEach { add("%L", it.toCodeBlock()) } })
        .build()

    fun buildString(): String = buildFile().toString()

}