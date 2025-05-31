package api

import core.context.BaseContext
import core.context.Context
import core.context.ContextKey
import dsl.domains.gradle.GradleDSLFactory
import dsl.framework.DSL
import dsl.framework.DSLFactory
import dsl.framework.DSLType
import languages.groovy.GroovyLanguageModule
import languages.kotlin.KotlinLanguage
import languages.kotlin.KotlinLanguageModule

/**
 * Менеджер для управления DSL фабриками и языковыми модулями
 */
class DSLManager {
    private val factories = mutableMapOf<DSLType, DSLFactory<*>>()
    private val kotlinLanguageModules = mutableMapOf<String, KotlinLanguageModule>()
    private val groovyLanguageModules = mutableMapOf<String, GroovyLanguageModule>()

    init {
        // Регистрируем встроенные фабрики
        registerFactory(DSLType.GRADLE, GradleDSLFactory())

        // Регистрируем встроенные языковые модули
        registerKotlinLanguageModule(KotlinLanguage.name, KotlinLanguageModule())
        registerGroovyLanguageModule("Groovy", GroovyLanguageModule())
    }

    /**
     * Регистрирует фабрику DSL
     */
    fun registerFactory(type: DSLType, factory: DSLFactory<*>) {
        factories[type] = factory
    }

    /**
     * Регистрирует модуль языка Kotlin
     */
    fun registerKotlinLanguageModule(name: String, module: KotlinLanguageModule) {
        kotlinLanguageModules[name] = module
    }

    /**
     * Регистрирует модуль языка Groovy
     */
    fun registerGroovyLanguageModule(name: String, module: GroovyLanguageModule) {
        groovyLanguageModules[name] = module
    }

    /**
     * Создает DSL указанного типа с поддержкой заданного языка
     */
    fun <D : DSL<*>> createDSL(type: DSLType, language: String): D {
        val factory = factories[type]
            ?: throw IllegalArgumentException("No factory registered for DSL type: $type")

        val context = when (language.lowercase()) {
            "kotlin" -> {
                val languageModule = kotlinLanguageModules[language]
                    ?: throw IllegalArgumentException("No Kotlin language module registered for: $language")
                createContext(languageModule)
            }

            "groovy" -> {
                val languageModule = groovyLanguageModules[language]
                    ?: throw IllegalArgumentException("No Groovy language module registered for: $language")
                createContext(languageModule)
            }

            else -> throw IllegalArgumentException("Unsupported language: $language")
        }

        @Suppress("UNCHECKED_CAST")
        return factory.create(context) as D
    }

    private fun createContext(languageModule: KotlinLanguageModule): Context {
        val context = BaseContext()
        context.bind(ContextKey.LANGUAGE, languageModule.language)
        context.bind(ContextKey.TYPE_SYSTEM, languageModule.typeSystem)
        return context
    }

    private fun createContext(languageModule: GroovyLanguageModule): Context {
        val context = BaseContext()
        context.bind(ContextKey.LANGUAGE, languageModule.language)
        context.bind(ContextKey.TYPE_SYSTEM, languageModule.typeSystem)
        return context
    }
}