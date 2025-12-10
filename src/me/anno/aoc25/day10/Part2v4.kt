package me.anno.aoc25.day10

import me.anno.utils.Utils.readLines
import java.io.File

// create files for an actual linear solver
//  -> a lame solution :(

fun main() {

    val lines = readLines(25, 10, "data.txt")
    val tasks = lines.map { it.parseTask() }

    var sum = 0

    for ((taskId0, task) in tasks.withIndex()) {

        val numButtons = task.buttonToLight.size
        val numLights = task.requirements.size

        val recipe = """
${
            (0 until numButtons).joinToString("\n") {
                "var x$it >= 0, integer;"
            }
        }

# Constraints
${
            (0 until numLights).joinToString("\n") { taskId ->
                val requirement = task.requirements[taskId]
                val buttons = task.buttonToLight.withIndex().filter { (buttonId, taskIds) ->
                    taskId in taskIds
                }.map { (buttonId, _) -> buttonId }
                "s.t. Eq$taskId: ${buttons.joinToString(" + ") { "x$it" }} = $requirement;"
            }
        }

minimize obj: ${(0 until numButtons).joinToString(" + ") { "x$it" }};

solve;
display ${(0 until numButtons).joinToString(", ") { "x$it" }};

    """.trimIndent()

        println(recipe)
        val file = File("./data/aoc25/day10/model$taskId0.mod")
        file.writeText(recipe)
        val process = ProcessBuilder("glpsol", "-m", file.absolutePath)
            .start()

        val solution = IntArray(numButtons) { -1 }
        val reader = process.inputStream.bufferedReader()
        while (true) {
            val line = reader.readLine() ?: break
            println(line)
            val midfix = ".val = "
            val k = line.indexOf(midfix)
            if (k >= 0) {
                val index = line.substring(1, k).toInt()
                solution[index] = line.substring(k + midfix.length).toInt()
            }
        }
        check(solution.all { it >= 0 })
        val best = solution.sum()
        println("Best: $best")
        sum += best
    }
    println("Total: $sum")
}