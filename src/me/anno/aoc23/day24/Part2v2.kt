package me.anno.aoc23.day24

import me.anno.utils.Utils.readLines
import java.math.BigDecimal
import kotlin.math.abs

fun main() {
    // todo find the position xyz and velocity abc, such that there is a t>0 for each hail stone, where it
    //  and the new ray intersect

    // idk how to solve this one...

    val lines = readLines(23, 24, "data.txt")
    val hails = lines.subList(1, lines.size)
        .map { parseHail(it) }

    val params = DoubleArray(6)

    fun getScore(i: Int, dv: Double): BigDecimal {
        val old = params[i]
        params[i] = old + dv

        val hailI = Hail(
            Vector3dx(
                BigDecimal(params[0]),
                BigDecimal(params[1]),
                BigDecimal(params[2])
            ),
            Vector3dx(
                BigDecimal(params[3]),
                BigDecimal(params[4]),
                BigDecimal(params[5])
            )
        )
        val err = error(hailI, hails)
        params[i] = old
        return err
    }

    var error = getScore(0, 0.0)
    fun apply(i: Int, dv: Double, err: BigDecimal) {
        params[i] += dv
        println("${error.toDouble()} -> ${err.toDouble()}, [$i] += $dv")
        error = err
    }

    val expansion = 1.5
    val contraction = 0.1
    for (j in 0 until 64) {
        var changed = false
        for (i in 0 until 6) {
            var pow = 1e15
            while (abs(pow) >= 1.0) {
                val p = getScore(i, +pow)
                if (p < error) {
                    apply(i, pow, p)
                    pow *= expansion
                    changed = true
                    continue
                }
                val m = getScore(i, -pow)
                if (m < error) {
                    apply(i, -pow, m)
                    pow *= -expansion
                    changed = true
                    continue
                }
                pow *= contraction
            }
        }
        if (!changed) break
    }

    println("final error: ${error.toDouble()}")

}

fun error(hail0: Hail, hails: List<Hail>): BigDecimal {
    return hails.sumOf { hail1 ->
        error(hail0, hail1)
    }
}

fun error(a: Hail, b: Hail): BigDecimal {
    val p = b.position - a.position
    val v = b.velocity - a.velocity
    val t = getT(p, v)
    val pa = a.position + a.velocity * t
    val pb = b.position + b.velocity * t
    return (pb - pa).lengthSquared()
}

fun getT(p: Vector3dx, v: Vector3dx): BigDecimal {
    val ax = abs(v.x)
    val ay = abs(v.y)
    val az = abs(v.z)
    val max = max(max(ax, ay), az)
    return when (max) {
        ax -> p.x / v.x
        ay -> p.y / v.y
        else -> p.z / v.z
    }
}

fun abs(a: BigDecimal): BigDecimal {
    return a.abs()
}

fun max(a: BigDecimal, b: BigDecimal): BigDecimal {
    return if (a > b) a else b
}