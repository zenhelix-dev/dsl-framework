package gradle.dsl.plugins.embedded

import gradle.dsl.core.AutoRegisterContext
import gradle.dsl.core.BasePolymorphicContainer
import gradle.dsl.core.DslBodyBlock
import gradle.dsl.core.DslElement

open class TasksDsl : DslBodyBlock("")

class TasksBlock(
    parent: DslElement,
    autoRegisterContext: AutoRegisterContext? = null,
    explicitProxyPath: String? = null
) : BasePolymorphicContainer<TasksDsl>(
    blockName = "tasks",
    parent = parent,
    explicitProxyPath = explicitProxyPath,
    autoRegisterContext = autoRegisterContext,
) {

}

