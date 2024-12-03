package me.anno.aoc24.utils

import java.io.InputStream

object Utils {

    private fun openStream(day: Int, name: String): InputStream {
        return Utils::class.java.classLoader
            .getResourceAsStream("./day$day/$name")!!
    }

    fun readText(day: Int, name: String): String {
        return openStream(day, name).use { stream ->
            stream.bufferedReader().readText()
        }
    }

    fun readLines(day: Int, name: String): List<String> {
        return openStream(day, name).use { stream ->
            stream.bufferedReader().readLines()
        }
    }

}