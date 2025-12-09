package me.anno.aoc25.day9

import me.anno.utils.Utils.readLines
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun isInsidePolygon(
    x: Int, y: Int, polygon: List<Corner>,
    i0: Int = 0, i1: Int = polygon.size - i0
): Boolean {
    var isInside = false
    val j = (i1 - 1)
    var jx = polygon[j].xi
    var jy = polygon[j].yi
    for (i in i0 until i1) {
        val ix = polygon[i].xi
        val iy = polygon[i].yi
        if ((iy > y) != (jy > y) &&
            (x - ix) < (jx - ix).toLong() * (y - iy) / (jy - iy)
        ) {
            isInside = !isInside
        }
        jx = ix
        jy = iy
    }
    return isInside
}

class Corner(val x: Int, val y: Int) {
    var xi = -1
    var yi = -1
}

/**
 * This is the smarty-bruteforce way:
 * we reduce the problem by making xs and ys unique,
 * then we just bruteforce the result
 * */
fun main() {
    val name = "data"
    val corners = readLines(25, 9, "$name.txt").map {
        val (x, y) = it.split(',').map { it.toInt() }
        Corner(x, y)
    }
    val xs = corners.map { it.x }.distinct().sorted()
    val ys = corners.map { it.y }.distinct().sorted()
    for (c in corners) {
        c.xi = xs.binarySearch(c.x) * 2
        c.yi = ys.binarySearch(c.y) * 2
    }

    val sx = xs.size * 2 + 1
    val sy = ys.size * 2 + 1
    val image = BufferedImage(sx, sy, 1)
    val isInsideLookup = BooleanArray(sx * sy)
    for (y in 0 until sy) {
        for (x in 0 until sx) {
            val inside = isInsidePolygon(x, y, corners)
            isInsideLookup[x + sx * y] = inside
            image.setRGB(x, y, if (inside) -1 else 0)
        }
    }

    for (c in corners) {
        image.setRGB(c.xi, c.yi, 0xff0000)
    }

    ImageIO.write(image, "png", File("./data/aoc25/day9/$name.png"))

    val combinations = corners.indices.flatMap { j ->
        corners.indices.mapNotNull { i ->
            if (i > j) Pair(i, j)
            else null
        }
    }.map { ij ->
        val (i, j) = ij
        val ci = corners[i]
        val cj = corners[j]
        val dx = abs(ci.x - cj.x) + 1L
        val dy = abs(ci.y - cj.y) + 1L
        ij to (dx * dy)
    }.sortedByDescending { it.second }
    search@ for (combination in combinations) {
        val c0 = corners[combination.first.first]
        val c1 = corners[combination.first.second]
        val x0 = min(c0.xi, c1.xi)
        val y0 = min(c0.yi, c1.yi)
        val x1 = max(c0.xi, c1.xi)
        val y1 = max(c0.yi, c1.yi)
        for (y in y0 + 1 until y1) {
            for (x in x0 + 1 until x1) {
                if (!isInsideLookup[x + sx * y]) continue@search
            }
        }

        println("Best: ${combination.second}")
        break
    }
}