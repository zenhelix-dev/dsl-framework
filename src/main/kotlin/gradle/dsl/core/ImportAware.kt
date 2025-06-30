package gradle.dsl.core

interface ImportAware {

    fun addImport(import: Import)

}

data class Import(
    val qualifiedName: String,
    val alias: String? = null
)