package gradle.dsl.plugins.embedded

import gradle.dsl.core.AutoRegisterContext
import gradle.dsl.core.BasePolymorphicContainer
import gradle.dsl.core.DslBodyBlock

open class TasksDsl : DslBodyBlock("")

class TasksBlock(
    autoRegisterContext: AutoRegisterContext? = null,
    explicitProxyPath: String? = null
) : BasePolymorphicContainer<TasksDsl>(
    blockName = "tasks",
    explicitProxyPath = explicitProxyPath,
    autoRegisterContext = autoRegisterContext,
) {

}


class TasksProxy(parentContext: AutoRegisterContext? = null) {
//        fun check(block: TaskConfigBlock.() -> Unit) = FunctionCall("tasks.check", body = TaskConfigBlock().apply(block))
//        fun named(name: String) = NamedTaskProxy(name)
//        fun jacocoTestReport(block: TaskConfigBlock.() -> Unit) = FunctionCall("tasks.jacocoTestReport", body = TaskConfigBlock().apply(block))
//
//        class NamedTaskProxy(private val name: String) {
//            operator fun invoke(block: TaskConfigBlock.() -> Unit) = FunctionCall("tasks.named(\"$name\")", body = TaskConfigBlock().apply(block))
//        }
}