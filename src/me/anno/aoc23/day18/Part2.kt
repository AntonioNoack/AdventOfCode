package me.anno.aoc23.day18

import me.anno.aoc24.day6.Vector2i
import me.anno.utils.Utils.readLines
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun parseLine2(line: String): Line {
    val j = line.indexOf(' ', 3)
    val length = line.substring(j + 3, line.length - 2).toInt(16)
    val dir = when (line[line.length - 2]) {
        '0' -> Direction.RIGHT
        '1' -> Direction.DOWN
        '2' -> Direction.LEFT
        '3' -> Direction.UP
        else -> throw IllegalStateException()
    }
    return Line(dir, length)
}

/**
 * do the same, just on a much larger scale...
 * analytically instead of using a fill-algorithm
 * */
fun main() {

    val lines = readLines(23, 18, "sample.txt")
        .map { parseLine2(it) }

    val corners = generateCornerPoints(lines)

    if (false) {
        // render everything onto image for debugging
        val sc = 16
        val image = BufferedImage(sc * 11, sc * 14, BufferedImage.TYPE_INT_RGB)
        val gfx = image.graphics

        gfx.translate(sc * 2, sc * 2)

        fun v(v: Vector2i): Vector2i {
            return (v * sc)
        }

        val outline = LinkedHashSet<Vector2i>()
        fillLinePoints(lines, outline)

        println(corners)
        println(outline)

        gfx.color = Color.RED
        for (v in outline) {
            val w = v(v)
            gfx.drawRect(w.x, w.y, sc, sc)
        }

        gfx.color = Color.GREEN
        for (i in corners.indices) {
            val a = v(corners[i])
            val b = v(corners[(i + 1) % corners.size])
            gfx.drawLine(a.x, a.y, b.x, b.y)
        }

        gfx.dispose()
        ImageIO.write(image, "png", File("C:/Users/Antonio/Desktop/day18.png"))
    }

    // sample: 952408394241 -> should be 952408144115 :(
    // 54058824809435 -> too high :(
    println(calculateArea(corners) + calculateLength(lines))
    println(calculateLength(lines))
}

fun generateCornerPoints(lines: List<Line>): List<Vector2i> {
    var position = Vector2i(1, 1)
    val result = ArrayList<Vector2i>()
    for (i in lines.indices) {
        result.add(position)
        val a = lines[(i + lines.lastIndex) % lines.size]
        val b = lines[(i) % lines.size]
        val c = lines[(i + 1) % lines.size]
        val dl = (sign(a, b) + sign(b, c)).shr(1)
        position += b.direction.v * (b.length + dl)
    }
    return result
}

fun sign(a: Line?, b: Line?): Int {
    if (a == null || b == null) return 0
    return b.direction.v.cross(a.direction.v)
}

fun calculateArea(corners: List<Vector2i>): Long {
    var sum = 0L
    for (i in 1 until corners.size) {
        val a = corners[i - 1]
        val b = corners[i]
        sum += a.crossL(b)
    }
    return sum.shr(1)
}

fun calculateLength(lines: List<Line>): Long {
    return lines.sumOf { it.length.toLong() }
}