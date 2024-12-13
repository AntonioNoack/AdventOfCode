package me.anno.aoc24.day13

import me.anno.utils.Matrix2i
import me.anno.utils.Vector2i
import java.math.BigInteger

fun main() {
    val machines = parseMachines("data.txt")
    val solutions = machines.mapNotNull { solveMachine2(it) }
    val prices = solutions.map { cost(it) }
    // solution: 89013607072065
    println("total: ${prices.sum()}")
}

val offset = BigInteger.valueOf(10000000000000)

fun List<BigInteger>.sum(): BigInteger {
    return reduce { a, b -> a + b }
}

val costVec2 = convert(costVec)
fun cost(v: Vector2ix): BigInteger {
    return v.dot(costVec2)
}

fun addOffset(v: Vector2i): Vector2ix {
    return Vector2ix(
        BigInteger.valueOf(v.x.toLong()) + offset,
        BigInteger.valueOf(v.y.toLong()) + offset
    )
}

fun convert(v: Vector2i): Vector2ix {
    return Vector2ix(
        BigInteger.valueOf(v.x.toLong()),
        BigInteger.valueOf(v.y.toLong())
    )
}

fun solveMachine2(machine: ClawMachine): Vector2ix? {
    // solve A*a + B*b = Price
    val (m, divisor0) = Matrix2i(
        machine.buttonA.x, machine.buttonB.x,
        machine.buttonA.y, machine.buttonB.y
    ).inverted()
    val divisor = BigInteger.valueOf(divisor0.toLong())
    val inv = m.transform(addOffset(machine.price))
    if (inv.x % divisor != BigInteger.ZERO || inv.y % divisor != BigInteger.ZERO) {
        // not solvable
        return null
    }
    return inv / divisor
}

fun Matrix2i.transform(v: Vector2ix): Vector2ix {
    return Vector2ix(mul(a, v.x) + mul(b, v.y), mul(c, v.x) + mul(d, v.y))
}

fun mul(i: Int, b: BigInteger): BigInteger {
    return b * BigInteger.valueOf(i.toLong())
}