package me.anno.aoc23.day25

import kotlin.math.abs
import kotlin.random.Random

// todo solve this faster than O(n³)
fun main() {

    val graph = readGraph("data.txt")
    println("#nodes: ${graph.connections.size}, #links: ${graph.links.size}")

    // todo choose two random nodes
    val (start, end) = graph.connections.keys.toList()
        .shuffled(Random(System.nanoTime()))
    var flow = initialFlow(graph, start, end)
    for (i in 0 until 10) {
        flow = averageFlow(graph, flow)
        val bestLinksToCut = flow.data.entries
            .sortedByDescending { abs(it.value) }
        println(bestLinksToCut.subList(0, 20))
    }

    val bestLinksToCut = flow.data.entries
        .sortedByDescending { abs(it.value) }

    val bestLinksToCut2 = bestLinksToCut.map { it.key }.subList(0, 20)

    bruteforce(graph, bestLinksToCut2)

    // todo calculate the best-balanced from between those nodes

}

fun averageFlow(graph: Graph, src: FlowMap): FlowMap {
    val dst = FlowMap()
    for ((node, links) in graph.connections) {
        var inFlow = 0.0
        var inFlowI = 0
        var outFlow = 0.0
        var outFlowI = 0
        for (con in links) {
            val flow = src[node, con]
            if (flow < 0) {
                inFlow += flow
                inFlowI++
            } else {
                outFlow += flow
                outFlowI++
            }
        }
        inFlow /= inFlowI
        outFlow /= outFlowI
        for (con in links) {
            val flow = src[node, con]
            dst[node, con] += 0.5 * if (flow < 0) inFlow else outFlow
        }
    }
    return dst
}

fun initialFlow(graph: Graph, start: String, end: String): FlowMap {
    var thisStage = HashSet<String>()
    var nextStage = HashSet<String>()
    val done = HashSet<String>()
    thisStage.add(start)
    done.add(start)
    val flow = FlowMap()
    val height = HeightMap()
    height[start] = 1.0
    while (thisStage.isNotEmpty()) {
        nextStage.clear()

        for (node in thisStage) {
            var numLinks = 0
            for (con in graph.connections[node] ?: emptySet()) {
                if (done.add(con)) {
                    nextStage.add(con)
                }
                if (con in nextStage) {
                    numLinks++
                }
            }

            if (numLinks == 0) {
                println("zero links!!")
            }
            // add that weights evenly onto the next nodes
            val perLink = height[node] / numLinks
            for (con in graph.connections[node] ?: emptySet()) {
                if (con in nextStage) {
                    height[con] += perLink
                    flow[node, con] = perLink
                }
            }
        }

        val tmp = nextStage
        nextStage = thisStage
        thisStage = tmp
    }
    return flow
}

class HeightMap {

    val data = HashMap<String, Double>()

    operator fun get(a: String): Double {
        return data[a] ?: 0.0
    }

    operator fun set(a: String, flow: Double) {
        data[a] = flow
    }
}

class FlowMap {

    val data = HashMap<Pair<String, String>, Double>()

    operator fun get(a: String, b: String): Double {
        if (a > b) return -get(b, a)
        return data[a to b] ?: 0.0
    }

    operator fun set(a: String, b: String, flow: Double) {
        if (a > b) return set(b, a, -flow)
        data[a to b] = flow
    }
}