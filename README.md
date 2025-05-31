# DSL Framework - Extensible Domain-Specific Language Builder

Модульная система для создания и генерации кода с использованием предметно-ориентированных языков (DSL). Поддерживает генерацию кода на Kotlin и
Groovy с возможностью расширения для новых языков и доменов.

## 🏗️ Архитектура

Проект разделён на независимые модули для максимальной гибкости и расширяемости:

```
new-improved-dsl/
├── dsl-core/              # Основной модуль - AST, контекст, фреймворк
├── dsl-kotlin-support/    # Поддержка языка Kotlin
├── dsl-groovy-support/    # Поддержка языка Groovy  
├── dsl-gradle/           # Gradle DSL реализация
├── dsl-api/              # Публичное API
└── dsl-examples/         # Примеры и тесты
```

### Модули

**dsl-core** - Ядро системы

- AST (Abstract Syntax Tree) с visitor и transformer паттернами
- Система контекстов и областей видимости
- Базовый DSL фреймворк
- Система типов
- Генерация кода

**dsl-kotlin-support** - Поддержка Kotlin

- Kotlin-специфичные AST узлы
- Генератор Kotlin кода
- Система типов Kotlin
- Языковые возможности (type inference, smart casts, etc.)

**dsl-groovy-support** - Поддержка Groovy

- Groovy-специфичные AST узлы (closures, maps, ranges)
- Генератор Groovy кода
- Groovy языковые особенности

**dsl-gradle** - Gradle DSL

- Gradle-специфичные строители и узлы
- Поддержка publishing, signing, dependencies, tasks
- Генерация Gradle скриптов

**dsl-api** - Публичное API

- DSLManager для управления фабриками
- Fluent API для удобного создания DSL
- Предустановленные конфигурации

## 🚀 Быстрый старт

### Базовое использование

```kotlin
import io.dmm.dsl.api.*
import io.dmm.dsl.gradle.GradleDSL
import io.dmm.dsl.gradle.references.MavenPublication
import io.dmm.dsl.gradle.references.components

val dslManager = DSLManager()

val gradleDSL = dslManager.create {
    type(DSLType.GRADLE)
    language("Kotlin")
    features {
        enableTypeInference()
        enableSmartCasts()
    }
} as GradleDSL

val buildScript = gradleDSL {
    publishing {
        repositories {
            mavenLocal()
            maven("https://repo.spring.io/release")
        }
        publications {
            create<MavenPublication>("java") {
                from(components["java"])
            }
        }
    }

    signing {
        useInMemoryPgpKeys("key-id", "password")
        sign(publishing.publications)
    }
}

val generatedCode = gradleDSL.generate(buildScript)
println(generatedCode)
```

### Предустановленные конфигурации

```kotlin
// Kotlin Gradle конфигурация
val kotlinDSL = dslManager.create(PresetConfigurations.kotlinGradle()) as GradleDSL

// Groovy Gradle конфигурация  
val groovyDSL = dslManager.create(PresetConfigurations.groovyGradle()) as GradleDSL

// Минимальная конфигурация
val minimalDSL = dslManager.create(PresetConfigurations.minimal()) as GradleDSL
```

### Кастомные конфигурации

```kotlin
val customDSL = dslManager.create {
    type(DSLType.GRADLE)
    language("Kotlin")
    configure {
        indentSize(2)
        useTabs(false)
        formatOutput(true)
        generateComments(true)
    }
    features {
        enableTypeInference()
        enableNullSafety()
        enableCoroutines()
    }
} as GradleDSL
```

## 📋 Возможности

### Поддерживаемые DSL типы

- ✅ **Gradle** - полная поддержка publishing, signing, dependencies, tasks
- 🔄 **Maven** - в планах
- 🔄 **Docker** - в планах
- 🔄 **Kubernetes** - в планах

### Поддерживаемые языки

- ✅ **Kotlin** - полная поддержка со всеми языковыми возможностями
- ✅ **Groovy** - поддержка closures, maps, ranges, GString
- 🔄 **Java** - в планах
- 🔄 **Scala** - в планах

### Gradle DSL возможности

```kotlin
gradleDSL {
    // Publishing
    publishing {
        repositories {
            mavenLocal()
            mavenCentral()
            maven("https://custom.repo.com") {
                name("CustomRepo")
                credentials("user", "pass")
            }
        }
        publications {
            create<MavenPublication>("java") {
                from(components["java"])
                artifactId("my-lib")
                groupId("com.example")
            }
        }
    }
    
    // Signing
    signing {
        useInMemoryPgpKeys("keyId", "password")
        sign(publishing.publications)
    }
    
    // Dependencies
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
        api("com.fasterxml.jackson.core:jackson-core:2.15.2")
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    }
    
    // Tasks
    tasks {
        register<Copy>("customTask") {
            // Task configuration
        }
        named("test") {
            // Test configuration
        }
    }
}
```

## 🔧 Расширение системы

### Добавление нового языка

1. Создайте модуль `dsl-mylang-support`
2. Реализуйте `LanguageModule`:

```kotlin
class MyLangLanguageModule : LanguageModule {
    override val language: Language = MyLangLanguage
    override val codeGenerator: CodeGenerator = MyLangCodeGenerator()
    override val typeSystem: TypeSystem = MyLangTypeSystem()
    
    override fun createVisitor(): NodeVisitor = MyLangNodeVisitor()
}
```

3. Зарегистрируйте в `DSLManager`:

```kotlin
dslManager.registerLanguageModule("MyLang", MyLangLanguageModule())
```

### Добавление нового DSL типа

1. Создайте модуль `dsl-mydomain`
2. Реализуйте `DSLFactory`:

```kotlin
class MyDomainDSLFactory : BaseDSLFactory<MyDomainDSL>() {
    override fun createDSL(context: DSLContext): MyDomainDSL {
        return MyDomainDSL(context)
    }
    
    override fun getDSLType(): DSLType = DSLType.MYDOMAIN
    override fun getCodeGenerator(): CodeGenerator = MyDomainCodeGenerator()
}
```

3. Зарегистрируйте в `DSLManager`:

```kotlin
dslManager.registerFactory(DSLType.MYDOMAIN, MyDomainDSLFactory())
```

## 🧪 Тестирование

Запуск всех тестов:

```bash
./gradlew test
```

Запуск тестов конкретного модуля:

```bash
./gradlew :dsl-examples:test
```

Примеры тестов включают:

- Базовую функциональность DSL
- Генерацию кода для Kotlin и Groovy
- Валидацию DSL структур
- Интеграционные тесты

## 📦 Сборка

Сборка всех модулей:

```bash
./gradlew build
```

Сборка конкретного модуля:

```bash
./gradlew :dsl-core:build
```

## 🤝 Вклад в проект

1. Форкните репозиторий
2. Создайте feature ветку
3. Внесите изменения
4. Добавьте тесты
5. Создайте Pull Request

## 📄 Лицензия

Этот проект распространяется под лицензией MIT.

## 🔗 Полезные ссылки

- [Kotlin DSL Reference](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
- [Groovy DSL Guide](https://docs.gradle.org/current/userguide/groovy_build_script_primer.html)
- [AST Pattern Documentation](https://en.wikipedia.org/wiki/Abstract_syntax_tree)
- [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern)