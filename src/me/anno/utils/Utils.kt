package me.anno.utils

import java.io.File
import java.io.InputStream

object Utils {

    private fun openStream(year: Int, day: Int, name: String): InputStream {
        return Utils::class.java.classLoader
            .getResourceAsStream("./aoc$year/day$day/$name")!!
    }

    fun readText(year: Int, day: Int, name: String): String {
        return openStream(year, day, name).use { stream ->
            stream.bufferedReader().readText()
        }
    }

    fun readLines(year: Int, day: Int, name: String): List<String> {
        return openStream(year, day, name).use { stream ->
            stream.bufferedReader().readLines()
        }
    }

    fun findPosition(lines: List<String>, searched: Char): Vector2i {
        for (y in lines.indices) {
            val line = lines[y]
            val x = line.indexOf(searched)
            if (x < 0) continue
            return Vector2i(x, y)
        }
        throw IllegalStateException("Missing '$searched'")
    }

    fun findPosition1(lines: List<CharArray>, searched: Char): Vector2i {
        for (y in lines.indices) {
            val line = lines[y]
            val x = line.indexOf(searched)
            if (x < 0) continue
            return Vector2i(x, y)
        }
        throw IllegalStateException("Missing '$searched'")
    }

    // up: 0, right: 1, down: 2, left: 3
    val directions = listOf(
        Vector2i(0, -1),
        Vector2i(1, 0),
        Vector2i(0, 1),
        Vector2i(-1, 0)
    )

    val user = File(System.getProperty("user.home"))
    val desktop = File(user, "Desktop")
}