package me.anno.aoc23.day20

import me.anno.utils.Utils.readLines

// needs analysis of the actual data, which then shows it has four or so subgraphs with clocks,
//  and when all are true together, we get rx
// -> I don't really want to code that, and sort the nodes by hand
fun main() {
    rxShouldCrash = true // second part
    val lines = readLines(23, 20, "data.txt") + suffix
    val modules = lines.map { parseModule(it) }
    val broadcaster = linkModules(lines, modules)
    val subgraphs = splitSubgraphs(modules)
    println(subgraphs.map { it.modules.size })
    val counts = subgraphs.map { getCount(broadcaster, it) }
    println(counts)
}

data class Subgraph(val dstModule: Module, val modules: List<Module>)

fun getCount(broadcaster: Broadcaster, subgraph: Subgraph): Int {
    val (_, modules) = subgraph
    for (module in modules) {
        module.reset()
    }
    var i = 0
    while (true) {
        try {
            buttonPress(modules, broadcaster)
        } catch (e: RuntimeException) {
            println("ex: $i")
            e.printStackTrace()
            return i
        }
        i++
    }
}

// remove all modules that don't influence rx
// not worth it, just removed one node out of 59...
fun splitSubgraphs(modules: List<Module>): List<Subgraph> {
    val getSources = getSources(modules)
    val rxModule = modules.filterIsInstance<Rx>().first()
    assert(getSources[rxModule]?.size == 1)
    val intoRxModule = getSources[rxModule]!!.first()
    assert(intoRxModule is Conjunction)
    val subgraphDst = getSources[intoRxModule]!!
    assert(subgraphDst.size == 4)
    return subgraphDst.map { dstModule ->
        println(dstModule)
        // why is this never called???
        dstModule.destinations.add(rxModule) // direct path to crash
        Subgraph(dstModule, findDependencies(dstModule, getSources))
    }
}
