package me.anno.aoc25.day12

import me.anno.utils.Utils.readLines
import java.io.File
import kotlin.concurrent.thread

data class Shape(
    val line0: String,
    val line1: String,
    val line2: String
) {
    fun rotated(): Shape {
        val (c0, c1, c2) = line0.toCharArray()
        val (c3, c4, c5) = line1.toCharArray()
        val (c6, c7, c8) = line2.toCharArray()
        return Shape(
            String(charArrayOf(c6, c3, c0)),
            String(charArrayOf(c7, c4, c1)),
            String(charArrayOf(c8, c5, c2)),
        )
    }

    fun mirroredX(): Shape {
        return Shape(line0.reversed(), line1.reversed(), line2.reversed())
    }

    fun mirroredY(): Shape {
        return Shape(line2, line1, line0)
    }

    operator fun get(x: Int, y: Int): Boolean {
        val line = when (y) {
            0 -> line0
            1 -> line1
            2 -> line2
            else -> throw IndexOutOfBoundsException()
        }
        return line[x] == '#'
    }
}

fun findAllShapes(shape: Shape): List<Shape> {
    val unique = HashSet<Shape>()
    val remaining = ArrayList<Shape>()
    unique.add(shape)
    remaining.add(shape)
    while (remaining.isNotEmpty()) {
        val curr = remaining.removeLast()
        for (next in listOf(curr.rotated(), curr.mirroredX(), curr.mirroredY())) {
            if (unique.add(next)) {
                remaining.add(next)
            }
        }
    }
    return unique.toList()
}

/**
 * A proper, true solution, but it is extremely slow,
 * taking multiple minutes(?) per sample, where we have 414 of them.
 * */
fun main() {
    // implement a proper solution with hundred thousands of equations
    // we need the following variables:
    //  - placement option
    // we need the following equations:
    //  - all placement options for each grid-tile is only placed 0 or 1 times
    //  - the total placements equal our target
    val problem = Problem(readLines(25, 12, "data.txt"))
    val shapes = problem.shapes.map { (a, b, c) ->
        findAllShapes(Shape(a, b, c))
    }
    println("Shapes: ${shapes.map { it.size }}, total: ${shapes.sumOf { it.size }}")
    val tasks = problem.tasks

    val file = File("./data/aoc25/day12/model.mod")
    // check viability by just filling up the field
    var ctr = 0
    for (task in tasks) {
        val builder = StringBuilder()
        for ((si, shape) in shapes.withIndex()) {
            for ((vi, _) in shape.withIndex()) {
                for (y in 0 until task.height - 2) {
                    for (x in 0 until task.width - 2) {
                        builder.append("var s${si}v${vi}x${x}y${y}, binary;\n")
                    }
                }
            }
        }

        for (y in 0 until task.height) {
            for (x in 0 until task.width) {
                builder.append("var F${x}F${y}, binary;\n")
            }
        }

        builder.append("\n# Constraints:\n")
        // validate the correct number of shapes
        for ((si, shape) in shapes.withIndex()) {
            builder.append("s.t. EqS$si: ")
            for ((vi, _) in shape.withIndex()) {
                for (y in 0 until task.height - 2) {
                    for (x in 0 until task.width - 2) {
                        builder.append("s${si}v${vi}x${x}y${y} + ")
                    }
                }
            }
            builder.setLength(builder.length - 3)
            builder.append(" = ").append(task.requirements[si]).append(";\n")
        }

        // validate that each cell is only filled once
        for (y in 0 until task.height) {
            for (x in 0 until task.width) {
                builder.append("s.t. EqX${x}Y${y}: ")
                for (dy in 0 until 3) {
                    val yi = y - dy
                    if (yi < 0) continue
                    for (dx in 0 until 3) {
                        val xi = x - dx
                        if (xi < 0) continue
                        if (xi >= task.width - 2 || yi >= task.height - 2) continue

                        for ((si, shape) in shapes.withIndex()) {
                            for ((vi, variant) in shape.withIndex()) {
                                if (variant[dx, dy]) {
                                    builder.append("s${si}v${vi}x${xi}y${yi} + ")
                                }
                            }
                        }
                    }
                }

                builder.setLength(builder.length - 3)
                builder.append(" = F${x}F${y};\n")
            }
        }

        builder.append("\nsolve;\n")
        builder.append("\nend;\n")

        val recipe = builder.toString()
        file.writeText(recipe)

        val process = ProcessBuilder("glpsol", "-m", file.absolutePath)
            .start()
        var fine = false
        thread {
            process.errorStream.bufferedReader().forEachLine { line ->
                System.err.println(line)
            }
        }
        process.inputStream.bufferedReader().forEachLine { line ->
            if (line == "INTEGER OPTIMAL SOLUTION FOUND") {
                fine = true
            }
            println(line)
        }

        println("Fine? $fine")
        if (fine) ctr++
    }
    // this solution was "correct" -> this is so stupid
    println("Fine: $ctr/${tasks.size}")

    file.delete()
}