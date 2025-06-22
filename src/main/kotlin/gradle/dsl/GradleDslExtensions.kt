package gradle.dsl

fun buildGradleKts(init: GradleBuildDslBuilder.() -> Unit = {}): String = GradleBuildDslBuilder().apply(init).buildString()

fun settingsGradleKts(init: GradleSettingsDslBuilder.() -> Unit = {}): String = GradleSettingsDslBuilder().apply(init).buildString()
