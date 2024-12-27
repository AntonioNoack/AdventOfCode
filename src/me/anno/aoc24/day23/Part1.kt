package me.anno.aoc24.day23

import me.anno.utils.Utils.readLines

class Node(val name: String) {
    val connections = HashSet<Node>()
    override fun toString(): String {
        return name
    }
}

fun readNodes(name: String): Map<String, Node> {
    val connections = readLines(24, 23, name)
    val nodes = HashMap<String, Node>()
    fun getNode(name: String): Node {
        return nodes.getOrPut(name) { Node(name) }
    }
    for (conn in connections) {
        val i = conn.indexOf('-')
        val a = getNode(conn.substring(0, i))
        val b = getNode(conn.substring(i + 1))
        a.connections.add(b)
        b.connections.add(a)
    }
    return nodes
}

fun main() {
    val nodes = readNodes("data.txt")
    val uniqueSets = HashSet<List<String>>()
    for ((_, node) in nodes) {
        if (!node.name.startsWith("t")) continue
        for (b in node.connections) {
            for (c in node.connections) {
                if (c in b.connections) {
                    val triple = listOf(node.name, b.name, c.name).sorted()
                    if (uniqueSets.add(triple)) {
                        println(triple)
                    }
                }
            }
        }
    }
    println("Total: ${uniqueSets.size}")
}