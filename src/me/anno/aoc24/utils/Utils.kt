package me.anno.aoc24.utils

import java.io.InputStream

object Utils {

    fun getStream(day: Int, name: String): InputStream {
        return Utils::class.java.classLoader
            .getResourceAsStream("./$day/$name")!!
    }

    fun getLines(day: Int, name: String): List<String> {
        return getStream(day, name).bufferedReader().readLines()
    }

}