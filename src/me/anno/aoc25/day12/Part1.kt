package me.anno.aoc25.day12

import me.anno.utils.Utils.readLines

// this tasks seems impossible...

class Task(val width: Int, val height: Int, val requirements: IntArray) {
    override fun toString(): String {
        return "$width x $height, ${requirements.toList()}"
    }
}

class Problem(lines: List<String>) {
    // parse data
    val shapes = List(6) { i ->
        val i0 = i * 5
        listOf(
            lines[i0 + 1],
            lines[i0 + 2],
            lines[i0 + 3],
        ).apply {
            forEach { check(it.length == 3) }
        }
    }
    val tasks = lines.subList(30, lines.size)
        .map {
            val c = it.indexOf(": ")
            val x = it.indexOf('x')
            val w = it.substring(0, x).toInt()
            val h = it.substring(x + 1, c).toInt()
            val requirements = it.substring(c + 2).split(' ').map { it.toInt() }
            check(requirements.size == 6)
            Task(w, h, requirements.toIntArray())
        }
}

fun main() {
    val problem = Problem(readLines(25, 12, "data.txt"))
    val shapes = problem.shapes
    val tasks = problem.tasks
    val minAmountFilled = shapes.minOf {
        it.sumOf { it.count { it == '#' } }
    }
    println("minAmount filled: $minAmountFilled")
    // check viability by just filling up the field
    var ctr = 0
    for (task in tasks) {
        val available = task.width * task.height
        val necessary = List(6) {
            val size = shapes[it].sumOf { it.count { it == '#' } }
            size * task.requirements[it]
        }.sum()
        val isFine = available >= necessary
        println("[$task] Viable from space alone? $isFine")
        ctr += if (isFine) 1 else 0
    }
    // this solution was "correct" -> this is so stupid
    println("Fine: $ctr/${tasks.size}")
}