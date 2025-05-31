package dsl.domains.gradle.references

/**
 * Ссылка на компонент Gradle
 */
interface ComponentReference {
    val name: String
}

/**
 * Объект для доступа к компонентам
 */
object components {
    operator fun get(name: String): ComponentReference {
        return object : ComponentReference {
            override val name: String = name
        }
    }
}

/**
 * Ссылка на блок publishing
 */
class PublishingReference {
    val publications = PublicationsReference()
}

/**
 * Ссылка на публикации
 */
class PublicationsReference {
    fun toPath(): String = "publishing.publications"
}

/**
 * Интерфейс Maven публикации
 */
interface MavenPublication

/**
 * Интерфейс Ivy публикации
 */
interface IvyPublication