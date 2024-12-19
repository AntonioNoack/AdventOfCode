package me.anno.aoc23.day25

// previous approach isn't working ->
// let's try the approach from janek37 https://www.reddit.com/r/adventofcode/comments/18qbsxs/comment/keump60/
// (would have never found that approach myself)
// part of it is that I wouldn't have expected it to work...
fun main() {
    val graph = readGraph("data.txt")
    for (node in graph.connections.keys) {
        trySplittingGraph(graph, node)
    }
}

fun trySplittingGraph(graph: Graph, node0: String) {
    val part1 = HashSet<String>()
    val edge = HashSet<String>()
    part1.add(node0)
    edge.addAll(graph.connections[node0]!!)
    while (edge.size > 3) {
        val nodeI = edge.minBy {
            score(graph, it, part1)
        }
        part1.add(nodeI)
        edge.remove(nodeI)
        for (con in graph.connections[nodeI]!!) {
            if (con !in part1) {
                edge.add(con)
            }
        }
    }
    println(edge)
    println(part1.size)
    println(graph.connections.size - part1.size)
    println(part1.size * (graph.connections.size - part1.size))
    throw IllegalStateException("Found solution :)")
}

fun score(graph: Graph, node: String, part1: HashSet<String>): Int {
    return graph.connections[node]!!.sumOf { con ->
        if (con in part1) -1L
        else +1L
    }.toInt()
}