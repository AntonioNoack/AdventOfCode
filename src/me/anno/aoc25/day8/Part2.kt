package me.anno.aoc25.day8

import me.anno.utils.Utils.readLines

fun main() {
    val boxes = readLines(25, 8, "data.txt")
        .map { it.parseBox() }
    val queue = createConnectionQueue(boxes)
    while (true) {
        val pair = queue.poll()!!
        if (mergeCircuits(pair.min, pair.max) && pair.max.circuit.size == boxes.size) {
            println("Connected all! $pair, ${Math.multiplyExact(pair.min.x, pair.max.x)}")
            break
        }
    }
}