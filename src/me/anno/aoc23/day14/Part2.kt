package me.anno.aoc23.day14

import me.anno.utils.Utils.readLines

val empty = '.'

data class State(val id: Int, val northWeight: Long)

fun main() {

    val field =
        readLines(23, 14, "data.txt")
            .map { it.toCharArray() }

    val north = North(field)
    val west = West(field)
    val south = South(field)
    val east = East(field)
    val states = HashMap<String, State>()
    val stateMap = ArrayList<State?>()
    var shortcut = false

    var prevState = State(0, getNorthWeight(field))
    states[getState(field)] = prevState

    for (i in 0 until 1000_000_000) {
        if (shortcut) {
            prevState = stateMap[prevState.id]!!
        } else {

            // simulation
            roll(north)
            roll(west)
            roll(south)
            roll(east)

            // serialize state & check whether we've reached a loop
            val state = getState(field)
            val stateI = states[state]
            val stateJ = if (stateI != null) {
                println("Reached loop after ${i + 1} iterations")
                shortcut = true
                stateI
            } else {
                val newState = State(states.size, getNorthWeight(field))
                states[state] = newState
                newState
            }

            while (stateMap.size <= prevState.id) {
                stateMap.add(null)
            }

            println("next[$prevState] = $stateJ")
            stateMap[prevState.id] = stateJ
            prevState = stateJ
            println(stateJ)
        }
    }

    // solution: State(id=116, northWeight=87273) after 119 iterations
    println(prevState)

}

fun getState(field: List<CharArray>): String {
    return field.joinToString("\n") { String(it) }
}

interface FieldAccess {
    val sx: Int
    val sy: Int
    operator fun get(y: Int, x: Int): Char
    operator fun set(y: Int, x: Int, value: Char)
}

class North(val lines: List<CharArray>) : FieldAccess {
    override val sx: Int get() = lines[0].size
    override val sy: Int get() = lines.size
    override fun get(y: Int, x: Int): Char = lines[y][x]
    override fun set(y: Int, x: Int, value: Char) {
        lines[y][x] = value
    }
}

class West(val lines: List<CharArray>) : FieldAccess {
    override val sx: Int get() = lines.size
    override val sy: Int get() = lines[0].size
    override fun get(y: Int, x: Int): Char = lines[x][y]
    override fun set(y: Int, x: Int, value: Char) {
        lines[x][y] = value
    }
}

class South(val lines: List<CharArray>) : FieldAccess {
    override val sx: Int get() = lines[0].size
    override val sy: Int get() = lines.size
    private val dy = sy - 1
    override fun get(y: Int, x: Int): Char = lines[dy - y][x]
    override fun set(y: Int, x: Int, value: Char) {
        lines[dy - y][x] = value
    }
}

class East(val lines: List<CharArray>) : FieldAccess {
    override val sx: Int get() = lines.size
    override val sy: Int get() = lines[0].size
    private val dy = sy - 1
    override fun get(y: Int, x: Int): Char = lines[x][dy - y]
    override fun set(y: Int, x: Int, value: Char) {
        lines[x][dy - y] = value
    }
}

fun roll(field: FieldAccess) {
    val sx = field.sx
    val sy = field.sy
    for (x in 0 until sx) {
        var balls = 0
        for (y in sy - 1 downTo 0) {
            when (field[y, x]) {
                stone -> {
                    for (i in 1..balls) {
                        field[y + i, x] = ball
                    }
                    balls = 0
                }
                ball -> {
                    field[y, x] = empty
                    balls++
                }
            }
        }
        val y = -1
        for (i in 1..balls) {
            field[y + i, x] = ball
        }
    }
}

fun getNorthWeight(field: List<CharArray>): Long {
    val sx = field[0].size
    val sy = field.size
    var sum = 0L
    for (y in sy - 1 downTo 0) {
        for (x in 0 until sx) {
            if (field[y][x] == ball) {
                sum += getBallWeight(y, sy)
            }
        }
    }
    return sum
}
