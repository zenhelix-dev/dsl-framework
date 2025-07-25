package gradle.dsl.plugins.kotlin

enum class JvmTarget(val target: String) {
    JVM_1_8("1.8"),
    JVM_9("9"),
    JVM_10("10"),
    JVM_11("11"),
    JVM_12("12"),
    JVM_13("13"),
    JVM_14("14"),
    JVM_15("15"),
    JVM_16("16"),
    JVM_17("17"),
    JVM_18("18"),
    JVM_19("19"),
    JVM_20("20"),
    JVM_21("21"),
    JVM_22("22"),
    JVM_23("23"),
    JVM_24("24")
    ;

    companion object {

        fun fromTarget(target: String): JvmTarget = entries.firstOrNull { it.target == target } ?: throw IllegalArgumentException(
            "Unknown Kotlin JVM target: $target,\navailable targets are ${
                entries.joinToString { it.target }
            }\n" + "Prefer configuring 'jvmTarget' value via 'compilerOptions' DSL: https://kotl.in/compiler-options-dsl"
        )

        val DEFAULT = JVM_1_8
    }
}