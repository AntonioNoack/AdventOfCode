package me.anno.aoc23.day5

import me.anno.utils.Utils.readLines

data class Section(val name: String, val maps: List<LongMap>) {
    fun map(src: Long): Long {
        for (map in maps) {
            if (map.contains(src)) {
                return map.map(src)
            }
        }
        return src
    }
}

data class LongMap(val dst0: Long, val src0: Long, val length: Long) {
    fun contains(src: Long): Boolean {
        return src in src0 until src0 + length
    }

    fun map(src: Long): Long {
        return src - src0 + dst0
    }
}

fun parseSeeds(lines: List<String>): List<Long> {
    return lines[0]
        .substring("seeds: ".length)
        .split(' ').map { it.toLong() }
}

fun parseSections(lines: List<String>): List<Section> {
    val sections = ArrayList<Section>()
    var i = 2
    while (i < lines.size) {
        val title = lines[i++]
        assert(title.endsWith(" map:"))
        val subRecipes = ArrayList<LongMap>()
        while (i < lines.size) {
            if (lines[i].isEmpty()) break
            val numbers = lines[i].split(' ').map { it.toLong() }
            assert(numbers.size == 3)
            subRecipes.add(LongMap(numbers[0], numbers[1], numbers[2]))
            i++
        }
        subRecipes.sortBy { it.src0 }
        sections.add(Section(title.substring(0, title.length - " map:".length), subRecipes))
        i++ // skip empty line
    }
    return sections
}

fun getMinSeedLocation(seed: Long, sections: List<Section>): Long {
    var index = seed
    for (section in sections) {
        index = section.map(index)
    }
    return index
}

fun main() {
    val lines = readLines(23, 5, "data.txt")
    assert(lines[0].startsWith("seeds: "))
    val seeds = parseSeeds(lines)
    val sections = parseSections(lines)
    val locations = seeds.map { seed ->
        getMinSeedLocation(seed, sections)
    }
    val bestLocation = locations.min()
    // solution: 806029445
    println(bestLocation)
}