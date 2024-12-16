package me.anno.aoc23.day24

import me.anno.utils.Utils.readLines
import java.math.BigDecimal

data class Hail(val position: Vector3dx, val velocity: Vector3dx)

fun main() {
    val lines = readLines(23, 24, "data.txt")
    val hails = lines.subList(1, lines.size)
        .map { parseHail(it) }
    for (hail in hails) {
        hail.position.z = BigDecimal.ZERO
        hail.velocity.z = BigDecimal.ZERO
    }
    val line0 = lines[0].split(',').map { BigDecimal(it) }
    val testArea = Vector2dx(line0[0], line0[1])
    // 5997 -> too low :(, threshold: < 1e-6
    // 13000, threshold: !> 0.99
    // 13149, threshold: 1.0, calculated using BigDecimal
    var ctr = 0
    for (i in hails.indices) {
        for (j in i + 1 until hails.size) {
            if (linesIntersectInTestArea(hails[i], hails[j], testArea)) {
                ctr++
            }
        }
    }
    println("#inter: $ctr out of ${hails.size}²/2")
}

fun linesIntersectInTestArea(a: Hail, b: Hail, testArea: Vector2dx): Boolean {
    return linesIntersectInTestArea(a.position, a.velocity, b.position, b.velocity, testArea)
}

fun linesIntersectInTestArea(
    pa: Vector3dx, va: Vector3dx, pb: Vector3dx, vb: Vector3dx,
    testArea: Vector2dx
): Boolean {
    val st = rayRayClosestTs(pa, va, pb, vb)
    if (st.x < BigDecimal.ZERO || st.y < BigDecimal.ZERO) return false
    val p1 = pa + va * st.x
    val p2 = pb + vb * st.y
    // println(listOf((p1 - p2).lengthSquared(), inTestArea(p1, testArea), inTestArea(p2, testArea)))
    if ((p1 - p2).lengthSquared() >= BigDecimal.ONE) {
        return false
    }
    return inTestArea(p1, testArea) && inTestArea(p2, testArea)
}

fun inTestArea(pos: Vector3dx, testArea: Vector2dx): Boolean {
    return pos.x in testArea.x..testArea.y &&
            pos.y in testArea.x..testArea.y
}

fun rayRayClosestTs(pos0: Vector3dx, dir0: Vector3dx, pos1: Vector3dx, dir1: Vector3dx): Vector2dx {
    val A = pos0
    val a = dir0
    val B = pos1
    val b = dir1
    val c = B - A
    val aa = a.lengthSquared()
    val bb = b.lengthSquared()
    val ac = a.dot(c)
    val ab = a.dot(b)
    val bc = b.dot(c)
    val div = aa * bb - ab * ab
    if (div.toDouble() == 0.0) {
        // parallel lines
        return Vector2dx(BigDecimal.ZERO, BigDecimal.ZERO)
    }
    val t = (ac * bb - bc * ab) / div
    val s = (ac * ab - bc * aa) / div
    return Vector2dx(t, s)
}

fun parseHail(line: String): Hail {
    // 19, 13, 30 @ -2,  1, -2
    val parts = line.split(',', '@')
        .map { BigDecimal(it.trim() + ".0") }
    assert(parts.size == 6)
    val position = Vector3dx(parts[0], parts[1], parts[2])
    val velocity = Vector3dx(parts[3], parts[4], parts[5])
    return Hail(position, velocity)
}