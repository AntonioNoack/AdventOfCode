package me.anno.aoc23.day25

import me.anno.utils.Utils.readLines

class Graph {

    val links = ArrayList<Pair<String, String>>()
    val connections = HashMap<String, HashSet<String>>()

    fun link1(a: String, b: String) {
        connections.getOrPut(a, ::HashSet).add(b)
    }

    fun link(a: String, b: String) {
        link1(a, b)
        link1(b, a)
    }

    fun link(pair: Pair<String, String>) {
        val (a, b) = pair
        link(a, b)
    }

    fun unlink1(a: String, b: String) {
        connections[a]?.remove(b)
    }

    fun unlink(a: String, b: String) {
        unlink1(a, b)
        unlink1(b, a)
    }

    fun unlink(pair: Pair<String, String>) {
        val (a, b) = pair
        unlink(a, b)
    }

}

fun readGraph(name: String): Graph {
    val graph = Graph()
    readLines(23, 25, name)
        .map { line ->
            val i = line.indexOf(": ")
            val key = line.substring(0, i)
            val values = line.substring(i + 2).split(' ')
            for (v in values) {
                graph.link(key, v)
                graph.links.add(key to v)
            }
        }
    return graph
}

// todo solve this faster than O(n³)
fun main() {
    val graph = readGraph("data.txt")
    bruteforce(graph, graph.links)
}

fun bruteforce(graph: Graph, links: List<Pair<String, String>>) {
    for (i in links.indices) {
        println("$i/${links.size}")
        val li = links[i]
        graph.unlink(li)
        for (j in links.indices) {
            // println("  $j") // 1/s, 1400 nodes -> this takes ~700 hours
            if (i == j) continue
            val lj = links[j]
            graph.unlink(lj)
            for (k in links.indices) {
                if (i == k || j == k) continue
                val lk = links[k]
                graph.unlink(lk)
                val connections = graph.connections
                val size = isSplit(connections)
                if (size < connections.size) {
                    val remaining = connections.size - size
                    println("$size x $remaining = ${size * remaining}")
                    return
                }
                graph.link(lk)
            }
            graph.link(lj)
        }
        graph.link(li)
    }
}

fun isSplit(connections: Map<String, Set<String>>): Int {
    val found = HashSet<String>()
    val remaining = ArrayList<String>()
    val start = connections.keys.first()
    found.add(start)
    remaining.add(start)
    while (true) {
        val next = remaining.removeLastOrNull() ?: break
        val links = connections[next] ?: continue
        for (link in links) {
            if (found.add(link)) {
                remaining.add(link)
            }
        }
    }
    return found.size
}