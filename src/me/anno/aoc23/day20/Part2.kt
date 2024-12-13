package me.anno.aoc23.day20

import me.anno.utils.Utils.readLines

// needs analysis of the actual data, which then shows it has four or so subgraphs with clocks,
//  and when all are true together, we get rx
// -> I don't really want to code that, and sort the nodes by hand
fun main() {
    rxShouldCrash = true // second part
    val lines = readLines(23, 20, "data.txt") + suffix
    val modules0 = lines.map { parseModule(it) }
    val broadcaster = linkModules(lines, modules0)
    val modules = filterModules(modules0)
    println("filter: ${modules0.size} -> ${modules.size}")
    for (i in 0 until 1000_000_000) {
        try {
            if ((i + 1) % (4096 * 32) == 0) {
                println(getState(modules))
            }
            buttonPress(modules, broadcaster)
            // println(getState(modules))
        } catch (e: RuntimeException) {
            e.printStackTrace()
            println("Crashed in turn $i")
            break
        }
    }
    println("low: $lowCtr, high: $highCtr")
    println(lowCtr * highCtr)
}

// remove all modules that don't influence rx
// not worth it, just removed one node out of 59...
fun filterModules(modules: List<Module>): List<Module> {
    val rxModule = modules.filterIsInstance<Rx>().first()
    return findDependencies(rxModule, getSources(modules))
}

fun getSources(modules: List<Module>): Map<Module, List<Module>> {
    val links = modules.flatMap { src ->
        src.destinations.map { dst ->
            src to dst
        }
    }
    return links
        .groupBy { (_, dst) -> dst }
        .mapValues { it.value.map { (src, _) -> src } }
}

fun gcd(x: Long, y: Long): Long {
    return if (y == 0L) x
    else gcd(y, x % y)
}

fun findDependencies(root: Module, getSources: Map<Module, List<Module>>): List<Module> {
    val required = LinkedHashSet<Module>()
    val remaining = ArrayList<Module>()
    remaining.add(root)
    required.add(root)
    while (true) {
        val next = remaining.removeLastOrNull() ?: break
        val sources = getSources[next] ?: continue
        for (src in sources) {
            if (required.add(src)) {
                remaining.add(src)
            }
        }
    }
    return required.toList()
}