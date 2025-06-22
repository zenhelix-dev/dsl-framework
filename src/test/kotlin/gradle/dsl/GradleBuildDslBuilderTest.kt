package gradle.dsl

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GradleBuildDslBuilderTest {

    @Test
    fun `gradle build success`() {
        val output = buildGradleKts {
            plugins {
                id("java")
            }

            subprojects {
                group = "com.example"

                publishing {
                    repositories {
                        mavenLocal()
                    }
                }

                signing {
                    useInMemoryPgpKeys("test-key", "test-pass")
                }
            }
        }

        assertEquals(
            """
        |plugins {
        |    id(java)
        |}
        |subprojects {
        |    group = com.example
        |    publishing {
        |        repositories {
        |            mavenLocal()
        |        }
        |    }
        |    signing {
        |        useInMemoryPgpKeys(test-key, test-pass)
        |    }
        |}
        |
        """.trimMargin(), output
        )
    }

}