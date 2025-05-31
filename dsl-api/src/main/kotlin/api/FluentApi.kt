package api

import dsl.framework.DSL
import dsl.framework.DSLType

/**
 * Fluent Builder для создания DSL
 */
class FluentDSLBuilder {
    private var dslType: DSLType = DSLType.GRADLE
    private var language: String = "Kotlin"
    private val features = mutableSetOf<String>()

    /**
     * Устанавливает тип DSL
     */
    fun type(type: DSLType): FluentDSLBuilder {
        this.dslType = type
        return this
    }

    /**
     * Устанавливает язык
     */
    fun language(lang: String): FluentDSLBuilder {
        this.language = lang
        return this
    }

    /**
     * Конфигурирует возможности языка
     */
    fun features(block: FeaturesBuilder.() -> Unit): FluentDSLBuilder {
        val builder = FeaturesBuilder()
        builder.block()
        features.addAll(builder.features)
        return this
    }

    /**
     * Создает DSL с помощью менеджера
     */
    fun <D : DSL<*>> build(manager: DSLManager): D {
        return manager.createDSL(dslType, language)
    }

    /**
     * Builder для настройки возможностей языка
     */
    class FeaturesBuilder {
        val features = mutableSetOf<String>()

        fun enableTypeInference() {
            features.add("type-inference")
        }

        fun enableSmartCasts() {
            features.add("smart-casts")
        }

        fun enableExtensionFunctions() {
            features.add("extension-functions")
        }

        fun enableOperatorOverloading() {
            features.add("operator-overloading")
        }

        fun enableNullableTypes() {
            features.add("nullable-types")
        }

        fun enableGroovyClosures() {
            features.add("groovy-closures")
        }

        fun enableGradlePlugins() {
            features.add("gradle-plugins")
        }
    }
}

/**
 * Extension функция для удобного создания DSL
 */
fun DSLManager.create(block: FluentDSLBuilder.() -> Unit): DSL<*> {
    val builder = FluentDSLBuilder()
    builder.block()
    return builder.build<DSL<*>>(this)
}