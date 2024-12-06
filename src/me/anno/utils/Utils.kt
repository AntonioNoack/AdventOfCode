package me.anno.utils

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

}