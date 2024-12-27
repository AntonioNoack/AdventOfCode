package me.anno.aoc24.day24

import me.anno.utils.Utils.readLines

val aliases = HashMap<String, String>()

data class Formula(val a: String, val type: String, val b: String, var result: String) {

    val min get() = minOf(a, b)
    val max get() = maxOf(a, b)

    val minName get() = minOf(name(a), name(b))
    val maxName get() = maxOf(name(a), name(b))
    val resultName get() = name(result)

    fun name(a: String) = aliases[a] ?: a
    override fun toString(): String {
        return "$minName ${type.padEnd(3)} $maxName -> ${name(result)}"
    }
}

fun readNetwork(name: String): Pair<Map<String, Boolean>, List<Formula>> {
    val lines = readLines(24, 24, name)
    val li = lines.indexOf("")
    val values = lines.subList(0, li).associate { line ->
        val i = line.indexOf(": ")
        line.substring(0, i) to (line[i + 2] == '1')
    }

    val formulas = lines.subList(li + 1, lines.size).map { line ->
        val parts = line.split(' ')
        if (parts.size != 5) throw IllegalStateException()
        if (parts[3] != "->") throw IllegalStateException()
        Formula(parts[0], parts[1], parts[2], parts[4])
    }

    return values to formulas
}

val xorType = "XOR"
val andType = "AND"
val orType = "OR"

fun main() {

    val (values0, formulas) = readNetwork("data.txt")

    val values = values0.toMutableMap()
    val logicByName = formulas.associateBy { it.result }
    fun getValue(name: String): Boolean {
        return values.getOrPut(name) {
            val logic = logicByName[name]!!
            val va = getValue(logic.a)
            val vb = getValue(logic.b)
            when (logic.type) {
                andType -> va and vb
                orType -> va or vb
                xorType -> va xor vb
                else -> throw IllegalStateException("Unknown operation ${logic.type}")
            }
        }
    }

    println(values)
    println(formulas)

    val result = (formulas.map { it.result } + values.keys)
        .filter { v -> v.startsWith("z") }
        .sortedDescending()
        .joinToString("") { v ->
            if (getValue(v)) "1" else "0"
        }.toLong(2)

    println(result)
    // solution: 63168299811048
}