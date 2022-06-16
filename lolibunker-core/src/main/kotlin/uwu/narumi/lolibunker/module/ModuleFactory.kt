package uwu.narumi.lolibunker.module

import uwu.narumi.lolibunker.module.impl.FastModule
import uwu.narumi.lolibunker.module.impl.SafeModule

object ModuleFactory {

    val defaultModule = SafeModule()
    val modules: Map<String, Module> = hashMapOf(
        Pair("safe", defaultModule),
        Pair("fast", FastModule())
    )

    fun fetch(name: String): Module {
        return modules.getOrDefault(name.lowercase(), defaultModule)
    }

    fun fetch(name: String, def: Module): Module {
        return modules.getOrDefault(name.lowercase(), def)
    }
}