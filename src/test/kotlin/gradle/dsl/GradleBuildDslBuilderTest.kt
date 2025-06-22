package gradle.dsl

import org.junit.jupiter.api.Test

class GradleBuildDslBuilderTest {

    @Test
    fun `gradle build success`() {
        val output = buildGradleKts {
            plugins {
                id("java")
            }

            subprojects {
                group = "com.example"
            }
        }
    }

}