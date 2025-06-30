package gradle.dsl

import gradle.dsl.plugins.embedded.IvyPublication
import gradle.dsl.plugins.embedded.MavenPublication
import gradle.dsl.plugins.kotlin.JvmTarget
import org.gradle.api.JavaVersion
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GradleBuildDslBuilderTest {

    @Test
    fun `gradle build success`() {
        val output = buildGradleKts {
            plugins {
                id("java")
            }

            group = "test"

            publishing {
                publications {
                    create("test", MavenPublication::class)
                }
            }
            signing {
                sign(publishing.publications)
            }
            publishing.publications.withType(MavenPublication::class) {
                pom {
                }
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
                    sign(publishing.publications)
                }

                publishing.publications.withType(MavenPublication::class) {
                    pom {
                    }
                }
            }
        }

        assertEquals(
            """
            |plugins {
            |    id("java")
            |}
            |group = "test"
            |publishing {
            |    publications {
            |        create("test", MavenPublication::class)
            |    }
            |}
            |signing {
            |    sign(publishing.publications)
            |}
            |publishing.publications.withType(MavenPublication::class) {
            |    pom {
            |    }
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
            |        sign(publishing.publications)
            |    }
            |    publishing.publications.withType(MavenPublication::class) {
            |        pom {
            |        }
            |    }
            |}
            |
        """.trimMargin(), output
        )
    }

    @Test
    fun `other gradle build success`() {
        val output = buildGradleKts {
            plugins {
                id("com.gradle.plugin-publish")
                `kotlin-dsl`
                signing
                `jacoco-report-aggregation`
                `jvm-test-suite`
            }

            group = "io.github.zenhelix"

            repositories {
                mavenCentral()
                gradlePluginPortal()
            }

            java {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17

                withJavadocJar()
                withSourcesJar()
            }

            kotlin {
                explicitApi()

                compilerOptions {
                    jvmTarget = JvmTarget.fromTarget(JavaVersion.VERSION_17.toString())
                }
            }

        }

        assertEquals(
            """
            
        """.trimMargin(), output
        )
    }
}