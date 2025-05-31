package api

/**
 * Конфигурация DSL
 */
data class DSLConfiguration(
    val indentSize: Int = 4,
    val useTabs: Boolean = false,
    val lineEnding: String = "\n",
    val includeImports: Boolean = true,
    val includePackage: Boolean = true
)

/**
 * Интерфейс для объектов, поддерживающих конфигурацию DSL
 */
interface DSLConfigurable {
    var configuration: DSLConfiguration
}