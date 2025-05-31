package languages.kotlin

import core.ast.BaseNodeVisitor
import core.ast.NodeVisitor
import core.context.Language
import core.generation.CodeGenerator
import core.types.BaseTypeSystem
import core.types.TypeSystem
import languages.kotlin.generation.KotlinCodeGenerator

/**
 * Модуль поддержки языка Kotlin
 */
class KotlinLanguageModule {
    val language: Language = KotlinLanguage
    val codeGenerator: CodeGenerator = KotlinCodeGenerator()
    val typeSystem: TypeSystem = KotlinTypeSystem()

    fun createVisitor(): NodeVisitor = KotlinNodeVisitor()
}

/**
 * Типовая система Kotlin
 */
class KotlinTypeSystem : TypeSystem by BaseTypeSystem()

/**
 * Посетитель для Kotlin AST
 */
class KotlinNodeVisitor : BaseNodeVisitor()