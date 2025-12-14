package me.anno.aoc25.day8

import me.anno.utils.Utils.readLines
import java.util.*

fun sq(x: Int): Long = x.toLong() * x.toLong()

data class Box(val x: Int, val y: Int, val z: Int, val id: Int) {
    var circuit = HashSet<Box>().apply { add(this@Box) } // reference to a set of boxes

    override fun toString(): String = "($x,$y,$z)"
}

data class BoxPair(val min: Box, val max: Box) {
    val distance = sq(min.x - max.x) + sq(min.y - max.y) + sq(min.z - max.z)
}

fun String.parseBox(id: Int): Box {
    val (x, y, z) = this
        .split(',')
        .map { it.toInt() }
    return Box(x, y, z, id)
}

fun  List<Box>.findShortestConnectionsExact(): Queue<BoxPair> {
    val options = indices.flatMap { x ->
        indices
            .filter { y -> y > x }
            .map { y -> BoxPair(this[x], this[y]) }
    }
    val queue = PriorityQueue<BoxPair>(options.size, compareBy { it.distance })
    queue.addAll(options)
    return queue
}

fun mergeCircuits(min: Box, max: Box): Boolean {
    if (min.circuit === max.circuit) return false // already connected

    min.circuit.addAll(max.circuit)
    max.circuit.forEach { it.circuit = min.circuit }
    // println("[${i + 1}] Joining ${pair.min} and ${pair.max} -> ${pair.min.circuit.size}")
    return true
}

fun List<String>.parseBoxes() =
    mapIndexed { index, line -> line.parseBox(index) }

fun main() {
    val file = "data.txt"
    val boxes = readLines(25, 8, file).parseBoxes()
    val queue = boxes.findShortestConnectionsExact()
    repeat(if (file == "data.txt") 1000 else 10) {
        val pair = queue.poll()!!
        mergeCircuits(pair.min, pair.max)
    }
    val circuits = boxes.map { it.circuit }
        .distinct()
        .map { it.size }
        .sortedDescending()
        .take(3)
    println(circuits)
    println(circuits.reduce { a, b -> a * b })
}