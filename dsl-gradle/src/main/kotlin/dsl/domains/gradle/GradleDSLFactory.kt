package dsl.domains.gradle

import core.generation.CodeGenerator
import dsl.domains.gradle.generation.GradleCodeGenerator
import dsl.framework.BaseDSLFactory
import dsl.framework.DSLContext
import dsl.framework.DSLType

/**
 * Фабрика для создания Gradle DSL
 */
class GradleDSLFactory : BaseDSLFactory<GradleDSL>() {

    override fun createDSL(context: DSLContext): GradleDSL {
        return GradleDSL(context)
    }

    override fun getDSLType(): DSLType = DSLType.GRADLE

    override fun getCodeGenerator(): CodeGenerator {
        return GradleCodeGenerator()
    }
}