package gradle.dsl.core

interface DslProvider<D : DslBlock> {
    fun createDsl(): D
}