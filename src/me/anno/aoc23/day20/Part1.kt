package me.anno.aoc23.day20

import me.anno.utils.Utils.readLines
import sun.reflect.generics.reflectiveObjects.NotImplementedException

enum class Signal {
    LOW, HIGH
}

var lowCtr = 0
var highCtr = 0

var rxShouldCrash = false

val suffix = listOf("output -> ", "rx -> ")

fun main() {
    // I have a weird "rx" in my data, which only appears as an output
    val lines = readLines(23, 20, "data.txt") + suffix
    val modules = lines.map { parseModule(it) }
    val broadcaster = linkModules(lines, modules)
    for (i in 0 until 1000) {
        buttonPress(modules, broadcaster)
    }
    println("low: $lowCtr, high: $highCtr")
    // answer: 768829480 -> too low :(, but we also have that weird rx-module...
    //         869395600 with weird rx-module -> correct :/
    println(lowCtr * highCtr)
}

fun linkModules(lines: List<String>, modules: List<Module>): Broadcaster {
    for (idx in lines.indices) {
        val line = lines[idx]
        val i0 = if (line[0] in "%&") 1 else 0
        val i1 = line.indexOf(" -> ")
        val name = line.substring(i0, i1)
        modules[idx].name = name
    }
    val byName = modules.associateBy { it.name }
    for (li in lines.indices) {
        val src = modules[li]
        val line = lines[li]
        val ix = line.indexOf(" -> ")
        val dst = line.substring(ix + 4).split(", ")
            .filter { it.isNotEmpty() }
            .map { name ->
                byName[name] ?: throw IllegalStateException("Missing '$name'")
            }
        src.destinations.addAll(dst)
        for (dstI in dst) {
            dstI.register(src)
        }
    }
    return byName["broadcaster"] as Broadcaster
}

fun parseModule(line: String): Module {
    return when {
        line.startsWith("broadcaster ") -> Broadcaster()
        line.startsWith("output ") -> Output()
        line.startsWith("rx ") -> Rx()
        line.startsWith("&") -> Conjunction()
        line.startsWith("%") -> FlipFlop()
        else -> throw NotImplementedException()
    }
}

fun simulateStep(modules: List<Module>) {
    for (i in modules.indices) {
        modules[i].process()
    }
    for (i in modules.indices) {
        modules[i].swap()
    }
}

fun buttonPress(modules: List<Module>, broadcast: Module) {
    broadcast.send(listOf(Signal.LOW), broadcast)
    while (true) {
        simulateStep(modules)
        if (modules.all { it.thisTurn.isEmpty() }) {
            return
        }
    }
}

fun getState(modules: List<Module>): BitStream {
    val dst = BitStream()
    for (module in modules) {
        module.serialize(dst)
    }
    return dst
}