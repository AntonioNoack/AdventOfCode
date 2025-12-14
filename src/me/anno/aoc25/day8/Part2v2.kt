package me.anno.aoc25.day8

import me.anno.utils.Utils.readLines
import java.util.*
import kotlin.math.max
import kotlin.math.min

// optimization idea: 3d grid for finding connections -> 25x faster on busy loop, 8x faster for one run (50 ms -> 7 ms)
// todo unionFind-algorithm for unions

// 13 is the max we can get away with
val gridSize = 11
fun getIndex(xi: Int, yi: Int, zi: Int): Int {
    return (xi + yi * gridSize) * gridSize + zi
}

fun List<Box>.findShortestConnectionsFast(): PriorityQueue<BoxPair> {
    val grid = Array(gridSize * gridSize * gridSize) { ArrayList<Box>() }
    val maxSize = 100_000
    for (i in indices) {
        val box = this[i]
        val xi = box.x * gridSize / maxSize
        val yi = box.y * gridSize / maxSize
        val zi = box.z * gridSize / maxSize
        check(xi in 0 until gridSize)
        check(yi in 0 until gridSize)
        check(zi in 0 until gridSize)
        grid[getIndex(xi, yi, zi)].add(box)
    }
    val queue = PriorityQueue<BoxPair>(1000, compareBy { it.distance })
    for (x1 in 0 until gridSize) {
        for (y1 in 0 until gridSize) {
            for (z1 in 0 until gridSize) {
                val boxes1 = grid[getIndex(x1, y1, z1)]
                if (boxes1.isEmpty()) continue
                // for this and all neighbor cells, add them to the queue
                for (z2 in max(z1 - 1, 0) until min(z1 + 2, gridSize)) {
                    for (y2 in max(y1 - 1, 0) until min(y1 + 2, gridSize)) {
                        for (x2 in max(x1 - 1, 0) until min(x1 + 2, gridSize)) {
                            val boxes2 = grid[getIndex(x2, y2, z2)]
                            for (i1 in boxes1.indices) {
                                val b1 = boxes1[i1]
                                for (i2 in boxes2.indices) {
                                    val b2 = boxes2[i2]
                                    if (b1.id < b2.id) {
                                        queue.add(BoxPair(b1, b2))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return queue
}

fun main() {
    val boxes = readLines(25, 8, "data.txt").parseBoxes()
    val t0 = System.nanoTime()
    repeat(99) { boxes.findShortestConnectionsFast() }
    val fast = boxes.findShortestConnectionsFast()
    val t1 = System.nanoTime()
    val exact = boxes.findShortestConnectionsExact()
    repeat(99) { boxes.findShortestConnectionsExact() }
    val t2 = System.nanoTime()
    println("${(t1 - t0) / 1e6f} ms fast vs ${(t2 - t1) / 1e6f} ms exact")
    for (i in 0 until 1000) {
        val fastI = fast.poll()!!
        val exactI = exact.poll()!!
        if (fastI != exactI) {
            throw IllegalStateException(
                "${fastI.distance} vs ${exactI.distance}@$i"
            )
        }
    }
}