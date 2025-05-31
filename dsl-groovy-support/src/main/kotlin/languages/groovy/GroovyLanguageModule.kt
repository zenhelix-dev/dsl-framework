package languages.groovy

import core.ast.BaseNodeVisitor
import core.ast.NodeVisitor
import core.context.Language
import core.generation.CodeGenerator
import core.types.BaseTypeSystem
import core.types.TypeSystem
import languages.groovy.generation.GroovyCodeGenerator

/**
 * Модуль поддержки языка Groovy
 */
class GroovyLanguageModule {
    val language: Language = GroovyLanguage
    val codeGenerator: CodeGenerator = GroovyCodeGenerator()
    val typeSystem: TypeSystem = GroovyTypeSystem()

    fun createVisitor(): NodeVisitor = GroovyNodeVisitor()
}

/**
 * Типовая система Groovy
 */
class GroovyTypeSystem : TypeSystem by BaseTypeSystem()

/**
 * Посетитель для Groovy AST
 */
class GroovyNodeVisitor : BaseNodeVisitor()