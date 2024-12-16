package me.anno.aoc24.day16

import me.anno.utils.Utils.directions
import me.anno.utils.Utils.findPosition
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import java.util.*

val start = 'S'
val end = 'E'
val wall = '#'

fun main() {
    val field = readLines(24, 16, "data.txt")
    findBestPath(field, false)
}

data class ScoreKey(val position: Vector2i, val dir: Int)
data class Node(val position: Vector2i, val dir: Int, val cost: Int) {
    override fun toString(): String {
        return "[$position, $dir, $cost]"
    }
}

fun findBestPath(field: List<String>, needsSeats: Boolean) {

    val remaining = PriorityQueue<Node> { a, b ->
        a.cost.compareTo(b.cost)
    }

    val startPos = findPosition(field, start)
    val startDir = directions.indexOf(Vector2i(1, 0))
    remaining.add(Node(startPos, startDir, 0))
    val sx = field[0].length
    val sy = field.size

    val backtrackMap = HashMap<Node, HashSet<Node>>()
    val bestScore = HashMap<ScoreKey, Int>()

    val bestSeats = HashSet<Vector2i>()
    if (needsSeats) {
        bestSeats.add(startPos)
        bestSeats.add(findPosition(field, end))
    }

    var bestCost = Int.MAX_VALUE
    while (remaining.isNotEmpty()) {
        val node0 = remaining.poll()
        val (pos0, dir0, cost0) = node0
        if (cost0 > bestCost) break
        // println("Handling $node0")
        for (deltaDir in -1..1) {
            val dir = (dir0 + deltaDir).and(3)
            val cost = cost0 + (if (deltaDir == 0) 1 else 1001)
            val pos = pos0 + directions[dir]
            if (pos.x in 0 until sx && pos.y in 0 until sy && cost <= bestCost) {
                val symbol = field[pos.y][pos.x]
                if (symbol != wall) {
                    if (symbol == end) {
                        if (cost < bestCost) {
                            // printPath(node0, backtrackMap, field)
                            println("Shortest path: $cost")
                            if (!needsSeats) return
                            bestCost = cost
                        }
                        markBestPaths(node0, backtrackMap, bestSeats)
                    } else {
                        // path -> continue
                        val scoreKey = ScoreKey(pos, dir)
                        val prevCost = bestScore[scoreKey]
                        if (prevCost == null || cost <= prevCost) {
                            val node = Node(pos, dir, cost)
                            backtrackMap.getOrPut(node, ::HashSet).add(node0)
                            if (prevCost == null || cost < prevCost) {
                                bestScore[scoreKey] = cost
                                remaining.add(node)
                            }
                        }
                    }
                }
            }
        }
    }

    if (bestCost == Int.MAX_VALUE) {
        throw IllegalStateException("End not found")
    } else if (needsSeats) {
        println("#bestSeats: ${bestSeats.size}")
    }
}

fun markBestPaths(node0: Node, backtrackMap: Map<Node, Set<Node>>, bestSeats: HashSet<Vector2i>) {
    val remaining = ArrayList<Node>()
    remaining.add(node0)
    while (true) {
        val node = remaining.removeLastOrNull() ?: break
        bestSeats.add(node.position)
        val prev = backtrackMap[node] ?: continue
        remaining.addAll(prev)
    }
}

fun printPath(node0: Node, backtrackMap: Map<Node, Set<Node>>, field: List<String>) {

    val pathToStart = HashSet<Node>()
    val remaining = ArrayList<Node>()
    remaining.add(node0)
    while (true) {
        val node = remaining.removeLastOrNull() ?: break
        pathToStart.add(node)
        val prev = backtrackMap[node] ?: continue
        remaining.addAll(prev)
    }

    val field1 = field.map { it.toCharArray() }
    val symbols = "^>v<"
    for ((node, dir) in pathToStart) {
        field1[node.y][node.x] = symbols[dir]
    }
    for (line in field1) {
        println(String(line))
    }
}