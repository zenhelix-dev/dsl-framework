package gradle.dsl.plugins.embedded

import gradle.dsl.core.DslBlock
import gradle.dsl.core.PropertyAssignment

class ProjectBlock(override val blockName: String = "") : DslBlock(blockName) {

    var group: String
        get() = throw UnsupportedOperationException("group is write-only in DSL context")
        set(value) {
            addChild(PropertyAssignment("group", value))
        }

}