package languages.kotlin

import core.context.Language
import core.context.LanguageFeature

/**
 * Описание языка Kotlin
 */
object KotlinLanguage : Language {
    override val name: String = "Kotlin"
    override val features: Set<LanguageFeature> = setOf(
        LanguageFeature.TYPE_INFERENCE,
        LanguageFeature.SMART_CASTS,
        LanguageFeature.EXTENSION_FUNCTIONS,
        LanguageFeature.OPERATOR_OVERLOADING,
        LanguageFeature.NULLABLE_TYPES
    )
}