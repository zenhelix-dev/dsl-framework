plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "dsl-framework"

include("dsl-core")
include("dsl-groovy-support")
include("dsl-kotlin-support")
include("dsl-api")

include("dsl-gradle")

include("dsl-examples")