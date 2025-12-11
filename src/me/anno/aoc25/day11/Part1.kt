package me.anno.aoc25.day11

import me.anno.utils.Utils.readLines

class Graph(lines: List<String>) {
    val nodeOutputs = lines
        .associate {
            val i = it.indexOf(": ")
            println(it.substring(i + 1).split(' '))
            it.substring(0, i) to it.substring(i + 1).trim().split(' ').map { node ->
                check(node.trim() == node)
                check(node.isNotEmpty())
                node
            }
        }
    val nodes = HashSet(nodeOutputs.keys)
    val connections: List<Pair<String, String>> =
        nodeOutputs.entries.flatMap { (node, outputs) -> outputs.map { output -> node to output } }
    val nodeInputs: Map<String, List<String>> = connections
        .groupBy { it.second }
        .map { (output, connections) ->
            output to connections.map { (input, _) -> input }
        }.toMap()

    init {
        println("nodes: $nodes")
        println("inputs: $nodeInputs")
    }

    fun countPaths(input: String, output: String, ignored: String? = null): Long {

        val reached = HashMap<String, Long>()
        reached[input] = 1L
        println("[$input] = 1")

        val remaining = HashSet(nodes)
        remaining.remove(input)

        while (true) {
            // find a node, which is only output of the checked nodes
            val node = remaining.firstOrNull { node ->
                (nodeInputs[node] ?: emptyList())
                    .all { input -> input in reached || input == ignored }
            } ?: break
            check(remaining.remove(node))
            val inputs = nodeInputs[node] ?: emptyList()
            val sum = inputs.sumOf { reached[it] ?: 0 }
            reached[node] = sum
            if (sum != 0L) println("[$node] = $sum")
        }

        val no = nodeInputs[output]
            ?: throw IllegalStateException("No node is reaching $output") // I loaded sample instead of sample2 for part2 😅
        val out = no.sumOf { reached[it] ?: 0L }
        println("[${input} -> $output] = $out")
        return out
    }
}

fun main() {
    // count how many times each node is reached
    Graph(readLines(25, 11, "data.txt"))
        .countPaths("you", "out")
}