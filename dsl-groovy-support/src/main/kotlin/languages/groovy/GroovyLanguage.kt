package languages.groovy

import core.context.Language
import core.context.LanguageFeature

/**
 * Описание языка Groovy
 */
object GroovyLanguage : Language {
    override val name: String = "Groovy"
    override val features: Set<LanguageFeature> = setOf(
        LanguageFeature.TYPE_INFERENCE,
        LanguageFeature.GROOVY_CLOSURES,
        LanguageFeature.GRADLE_PLUGINS
    )
}