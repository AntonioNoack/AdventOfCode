package me.anno.aoc23.day15

import me.anno.utils.Utils.readText

fun hash(string: String): Int {
    var hash = 0
    for (symbol in string) {
        hash = hash(hash, symbol)
    }
    return hash
}

fun hash(hash: Int, symbol: Char): Int {
    return ((hash + symbol.code) * 17).and(255)
}

fun readParts(name: String): List<String> {
    return readText(23, 15, name)
        .replace("\r", "").replace("\n", "")
        .split(',')
}

fun main() {
    val data = readParts("data.txt")
    println(data.map { hash(it) })
    println(data.sumOf { hash(it) })
}