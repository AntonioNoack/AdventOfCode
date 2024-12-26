package me.anno.aoc24.day21

import me.anno.utils.Utils.directions
import me.anno.utils.Utils.findPosition
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import java.util.*

class Keypad(val keys: List<String>) {

    val sx = keys[0].length
    val sy = keys.size

    // simulation
    var dstI = 0
    var searched = ""
    var next: Keypad? = null
    var pos = 0

    fun activate(): Boolean {
        val next = next
        if (next != null) {
            return when (val c1 = flat[pos]) {
                in "<^v>" -> {
                    next.pos = next.validLinksByKey[next.pos][c1] ?: return false
                    true
                }
                'A' -> next.activate()
                else -> false
            }
        } else {
            // check char with expected
            val expected = searched[dstI]
            return if (expected == flat[pos]) {
                dstI++
                true
            } else false
        }
    }

    // data

    fun toFlat0(v: Vector2i): Int {
        return v.x + sx * v.y
    }

    fun toFlat(v: Vector2i): Int? {
        return if (v.x in 0 until sx && v.y in 0 until sy) toFlat0(v) else null
    }

    val flat = keys.joinToString("")
    val start = flat.indexOf('A')

    val validLinks: List<IntArray> =
        keys.flatMapIndexed { y, line ->
            line.mapIndexed { x, _ ->
                val pos = Vector2i(x, y)
                directions.mapNotNull { dir ->
                    val pos2 = pos + dir
                    val idx = toFlat(pos2)
                    if (idx != null && keys[pos2.y][pos2.x] != ' ') idx
                    else null
                }.toIntArray()
            }
        }

    val validLinksByKey: List<Map<Char, Int>> =
        keys.flatMapIndexed { y, line ->
            line.mapIndexed { x, _ ->
                val pos = Vector2i(x, y)
                directions2.mapNotNull { (key, dir) ->
                    val pos2 = pos + dir
                    val idx = toFlat(pos2)
                    if (idx != null && keys[pos2.y][pos2.x] != ' ') key to idx
                    else null
                }.toMap()
            }
        }

}

val directions2 = mapOf(
    '<' to Vector2i(-1, 0),
    '>' to Vector2i(+1, 0),
    '^' to Vector2i(0, -1),
    'v' to Vector2i(0, +1)
)

val numericKeys = "789,456,123, 0A".split(',')
val arrowKeys = " ^A,<v>".split(',')

fun main() {
    val kp0 = Keypad(numericKeys)
    val kp1 = Keypad(arrowKeys)
    val kp2 = Keypad(arrowKeys)
    println(translate("<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A", listOf(kp2, kp1, kp0)))
    val lines = readLines(24, 21, "data.txt")
    var score = 0
    for (line in lines) {
        val steps = findShortestTranslation(listOf(kp2, kp1, kp0), line)
        println("$steps for $line")
        score += steps * line.filter { it in '0'..'9' }.toInt()
    }
    println("Score: $score")
    // solution: 211930
}

data class Position(
    val positions: List<Int>,
    val stepCounter: Int,
    val dstI: Int
) {
    // todo can we define a better scoring function???
    val score get() = stepCounter - dstI * 100_000_000
}

fun isSolution(searchedDst: String, position: Position): Boolean {
    return searchedDst.length == position.dstI
}

fun findShortestTranslation(
    keypads: List<Keypad>,
    searchedDst: String
): Int {

    for (i in 1 until keypads.size) {
        keypads[i - 1].next = keypads[i]
    }
    keypads.last().searched = searchedDst

    val start = Position(
        keypads.map { it.start },
        0, 0
    )

    val enqueued = HashSet<Position>()
    val queue = PriorityQueue<Position> { a, b ->
        a.score.compareTo(b.score)
    }
    queue.add(start)
    enqueued.add(start)

    val options = intArrayOf(1, 2, 3, 4, 5)
    while (true) {
        val here = queue.poll() ?: break
        if (isSolution(searchedDst, here)) {
            println("  tried ${enqueued.size} combinations")
            return here.stepCounter
        }
        for (option in options) {
            // find next positions
            val cost = distance(option, here.positions[0]) + 1
            val next = apply(here, option, cost, keypads)
            next ?: continue
            if (enqueued.add(next)) {
                queue.add(next)
            }
        }
    }

    return -1
}

val iToPos = listOf(
    Vector2i(-1, -1),
    Vector2i(1, 0),
    Vector2i(2, 0),
    Vector2i(0, 1),
    Vector2i(1, 1),
    Vector2i(2, 1)
)

fun distance(ia: Int, ib: Int): Int {
    val pa = iToPos[ia]
    val pb = iToPos[ib]
    return pa.manhattanDistance(pb)
}

fun apply(
    position: Position, option: Int, cost: Int,
    keypads: List<Keypad>
): Position? {
    val (positions, stepCounter, dstI) = position
    keypads[0].pos = option
    for (i in 1 until keypads.size) {
        keypads[i].pos = positions[i]
    }
    keypads.last().dstI = dstI
    return if ( keypads[0].activate()) {
        Position(
            keypads.map { it.pos },
            stepCounter + cost,
            keypads.last().dstI
        )
    } else null

}

fun check(cursor: Vector2i, dst: Keypad): Char {
    val c = dst.keys[cursor.y][cursor.x]
    if (c == ' ') throw IllegalStateException()
    return c
}

fun translate(sequence: String, dst: List<Keypad>): String {
    var sequenceI = sequence
    for (kp in dst) {
        sequenceI = translate(sequenceI, kp)
    }
    return sequenceI
}

fun translate(sequence: String, dst: Keypad): String {
    val result = StringBuilder()
    val cursor = findPosition(dst.keys, 'A')
    for (cmd in sequence) {
        when (cmd) {
            '<' -> cursor.x--
            '>' -> cursor.x++
            '^' -> cursor.y--
            'v' -> cursor.y++
            'A' -> result.append(check(cursor, dst))
            else -> throw IllegalArgumentException("$cmd")
        }
        check(cursor, dst)
    }
    return result.toString()
}