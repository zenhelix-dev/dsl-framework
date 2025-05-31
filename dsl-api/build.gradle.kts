description = "DSL API - публичное API для работы с DSL"

dependencies {
    api(project(":dsl-core"))
    api(project(":dsl-kotlin-support"))
    api(project(":dsl-groovy-support"))
    api(project(":dsl-gradle"))
}