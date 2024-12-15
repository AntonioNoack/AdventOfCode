package me.anno.aoc23.day22

import me.anno.utils.Utils.readLines
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.sqrt

fun main() {

    // z is up...
    val bricks = readLines(23, 22, "data.txt").map { parseBrick(it) }
    val (dependencies, invDependencies) = findDependencies(bricks)

    letBricksFall(bricks, dependencies)

    // for each brick, check how many others would fall, if it was to be removed
    val supportBricks = findSupportBricks(dependencies)
    val wouldFallIfRemoved = HashMap<Brick, Set<Brick>>()
    for (lower in bricks.sortedByDescending { it.min.z }) {

        val uppers0 = invDependencies[lower] ?: continue

        val uppers = PriorityQueue<Brick> { a, b -> a.min.z.compareTo(b.min.z) }
        uppers.addAll(uppers0)

        val enqueued = HashSet<Brick>()
        enqueued.addAll(uppers0)

        val removed = HashSet<Brick>()
        removed.add(lower)

        fun check(upper: Brick) {
            if (supportBricks[upper]!!.all { lower -> lower in removed }) {
                // yes, this falls
                removed.add(upper)
                // handle all blocks above
                val uppers1 = invDependencies[upper]
                if (uppers1 != null) {
                    for (upperI in uppers1) {
                        if (enqueued.add(upperI)) {
                            uppers.add(upperI)
                        }
                    }
                }
            }
        }

        while (uppers.isNotEmpty()) {
            check(uppers.poll())
        }

        removed.remove(lower)
        wouldFallIfRemoved[lower] = removed.toHashSet()
    }

    val maxNumBlocksFalling = wouldFallIfRemoved.values.maxOf { it.size }
    println("max num blocks falling: $maxNumBlocksFalling")

    writeBricks(bricks, "Falling2") { brick ->
        val f = (wouldFallIfRemoved[brick] ?: emptyList()).size.toFloat() / maxNumBlocksFalling
        if (f < 0.5f) mixARGB(0xffffff, 0xff0000, sqrt(f * 2f))
        else mixARGB(0xff0000, 0x000000, sqrt(f * 2f - 1f))
    }

    println("fall sum: ${wouldFallIfRemoved.values.sumOf { it.size }}")
    // solution: 15257 -> too low :(, why?, because we forgot that if multiple blocks fall, all supports might be gone
    //           51733 -> correct :)
}

fun mixChannel(ca: Int, cb: Int, f: Float, shift: Int): Int {
    val mask = 0xff shl shift
    val a = ca.and(mask)
    val b = cb.and(mask)
    return (a + (b - a) * f).toInt().and(mask)
}

fun mixARGB(ca: Int, cb: Int, f: Float): Int {
    return mixChannel(ca, cb, f, 0) or
            mixChannel(ca, cb, f, 8) or
            mixChannel(ca, cb, f, 16)
}
