package me.anno.aoc23.day23

import me.anno.utils.Utils.readLines

// again, this is waaayy too easy xD
fun main() {
    val field = readLines(23, 23, "data.txt")
        .map(::removeSlopes)
    println(findLongestPathByField(field))
}

fun removeSlopes(line: String): String {
    return String(CharArray(line.length) {
        if (line[it] == wall) wall else path
    })
}
