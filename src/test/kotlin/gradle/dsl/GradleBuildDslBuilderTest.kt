package gradle.dsl

import gradle.dsl.plugins.embedded.IvyPublication
import gradle.dsl.plugins.embedded.MavenPublication
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
                    publications {
                        register("test", MavenPublication::class) {
                            groupId = "com.example.test"
                        }
                        create("test", MavenPublication::class) {

                        }
                        create("test", IvyPublication::class)
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
            |    id("java")
            |}
            |subprojects {
            |    group = "com.example"
            |    publishing {
            |        repositories {
            |            mavenLocal()
            |        }
            |        publications {
            |            register("test", MavenPublication::class) {
            |                groupId = "com.example.test"
            |            }
            |            create("test", MavenPublication::class) {
            |            }
            |            create("test", IvyPublication::class)
            |        }
            |    }
            |    signing {
            |        useInMemoryPgpKeys("test-key", "test-pass")
            |    }
            |}
            |
        """.trimMargin(), output
        )
    }

}