package me.anno.aoc24.day6

import me.anno.utils.Utils.findPosition

class Field(lines: List<String>) {
    val sx = lines[0].length
    val sy = lines.size
    val start = findPosition(lines, agent)
    val lines = lines.map {
        it.toMutableList()
    }
}