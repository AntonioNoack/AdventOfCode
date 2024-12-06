package me.anno.aoc24.day6

class Field(lines: List<String>) {
    val sx = lines[0].length
    val sy = lines.size
    val start = findAgentPosition(lines)
    val lines = lines.map {
        it.toMutableList()
    }
}