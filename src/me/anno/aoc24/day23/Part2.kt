package me.anno.aoc24.day23

fun main() {
    // find the largest set of all-connected computers...
    val nodes = readNodes("data.txt")
    val bestSet = nodes.values
        .map { node -> findLargestSet(setOf(node)) }
        .maxBy { it.size }
    println("Total: ${bestSet.size}, '${bestSet.map { it.name }.sorted().joinToString(",")}'")
}

fun findLargestSet(nodes: Set<Node>): Set<Node> {
    val sample = nodes.first()
    var bestSet = nodes
    for (candidate in sample.connections) {
        if (candidate !in nodes &&
            candidate !in bestSet && // is this like correct? speeds up the search at least, and we apparently have the correct answer
            nodes.all { node -> candidate in node.connections }
        ) {
            // good -> can be added
            val nextSet = findLargestSet(nodes + candidate)
            if (nextSet.size > bestSet.size) {
                bestSet = nextSet
            }
        }
    }
    return bestSet
}