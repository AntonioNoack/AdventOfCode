package me.anno.aoc24.day13

import me.anno.utils.Matrix2i
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

fun main() {
    val machines = parseMachines("data.txt")
    println(machines)
    val solutions = machines.mapNotNull { solveMachine(it) }
    println(solutions)
    val prices = solutions
        .filter { isWithinLimit(it) }
        .map { cost(it) }
    println(prices)
    // solution: 32026
    println("total: ${prices.sum()}")
}

fun parseMachines(name: String): List<ClawMachine> {
    val lines = readLines(24, 13, name)
    var i = 0
    val result = ArrayList<ClawMachine>((lines.size + 3) / 4)
    while (i + 2 < lines.size) {
        val ba = lines[i]
        val bb = lines[i + 1]
        val pr = lines[i + 2]
        result.add(ClawMachine(parseButton(ba), parseButton(bb), parsePrize(pr)))
        i += 4 // skip four lines
    }
    return result
}

fun parseButton(line: String): Vector2i {
    val i0 = line.indexOf('X')
    val i1 = line.indexOf(',', i0)
    val i2 = line.indexOf('Y', i1)
    val x = line.substring(i0 + 1, i1).toInt()
    val y = line.substring(i2 + 1).toInt()
    return Vector2i(x, y)
}

fun parsePrize(line: String): Vector2i {
    val i0 = line.indexOf("X=")
    val i1 = line.indexOf(',', i0)
    val i2 = line.indexOf("Y=", i1)
    val x = line.substring(i0 + 2, i1).toInt()
    val y = line.substring(i2 + 2).toInt()
    return Vector2i(x, y)
}

fun solveMachine(machine: ClawMachine): Vector2i? {
    // solve A*a + B*b = Price
    val (m, divisor) = Matrix2i(
        machine.buttonA.x, machine.buttonB.x,
        machine.buttonA.y, machine.buttonB.y
    ).inverted()
    if (divisor == 0) {
        return null
    }
    val inv = m.transform(machine.price)
    if (inv.x % divisor != 0 || inv.y % divisor != 0) {
        // not solvable
        return null
    }
    return inv / divisor
}

fun isWithinLimit(v: Vector2i): Boolean {
    return v.x in 0..pressLimit && v.y in 0..pressLimit
}

val pressLimit = 100

val costVec = Vector2i(3, 1)
fun cost(v: Vector2i): Int {
    return v.dot(costVec)
}

data class ClawMachine(val buttonA: Vector2i, val buttonB: Vector2i, val price: Vector2i)