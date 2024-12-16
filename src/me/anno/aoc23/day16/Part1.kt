package me.anno.aoc23.day16

import me.anno.utils.Utils.directions
import me.anno.utils.Vector2i
import me.anno.utils.Utils.readLines

data class Ray(val position: Vector2i, val dir: Int) {

    fun step(): Ray {
        return turn(dir)
    }

    fun turn(dir: Int): Ray {
        return Ray(position + directions[dir], dir)
    }

    fun wrap(): List<Ray> = listOf(this)

    fun step(field: List<String>): List<Ray> {

        val map1 = intArrayOf(1, 0, 3, 2)
        val map2 = intArrayOf(3, 2, 1, 0)

        val dir = when (field[position.y][position.x]) {
            '.' -> dir
            '/' -> map1[dir]
            '\\' -> map2[dir]
            '-' -> {
                if (dir == 0 || dir == 2) {
                    // split
                    return listOf(
                        turn(1),
                        turn(3)
                    )
                } else dir // just continue
            }
            '|' -> {
                if (dir == 1 || dir == 3) {
                    return listOf(
                        turn(0),
                        turn(2)
                    )
                } else dir // just continue
            }
            else -> throw NotImplementedError()
        }
        return turn(dir).wrap()
    }

    fun isValid(field: List<String>): Boolean {
        return position.x in field[0].indices &&
                position.y in field.indices
    }

}

fun numEnergizedTiles(startRay: Ray, field: List<String>): Int {
    val remaining = ArrayList<Ray>()
    val rays = HashSet<Ray>()

    remaining.add(startRay)
    rays.add(startRay)

    while (true) {
        val ray = remaining.removeLastOrNull() ?: break
        val next = ray.step(field)
        for (rayI in next) {
            if (rayI.isValid(field) && rays.add(rayI)) {
                remaining.add(rayI)
            }
        }
    }

    return rays.distinctBy { it.position }.size
}

fun main() {
    val field = readLines(23, 16, "data.txt")
    val start = Ray(Vector2i(0, 0), 1)
    println(numEnergizedTiles(start, field))
}