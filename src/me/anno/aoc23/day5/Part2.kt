package me.anno.aoc23.day5

import me.anno.utils.Utils.readLines
import kotlin.math.min

class LongRange(val src0: Long, val length: Long)

fun parseSeeds2(lines: List<String>): List<LongRange> {
    val seeds = parseSeeds(lines)
    return (0 until seeds.size / 2).map {
        LongRange(seeds[it * 2], seeds[it * 2 + 1])
    }
}

fun getMinSeedLocation(seeds: LongRange, sections: List<Section>): Long {
    var index = setOf(seeds)
    for (section in sections) {
        index = index
            .map { range -> section.map(range) }
            .flatten().toHashSet()
    }
    return index.minOf { it.src0 }
}

fun Section.map(seeds: LongRange): Set<LongRange> {
    var src0 = seeds.src0
    val src1 = seeds.src0 + seeds.length
    val result = HashSet<LongRange>()
    for (map in maps) {
        if (map.src0 + map.length < src0) {
            continue // can be skipped
        }
        if (map.src0 >= src1) {
            continue // can be skipped
        }
        // we're colliding with it maybe
        // add start
        if (map.src0 > src0) {
            result.add(LongRange(src0, map.src0 - src0))
            src0 = map.src0
        }
        // add mapped
        val end = min(map.src0 + map.length, src1)
        if (src0 < end) {
            result.add(LongRange(src0 - map.src0 + map.dst0, end - src0))
            src0 = end
        }
    }
    // add end
    if (src1 > src0) {
        result.add(LongRange(src0, src1 - src0))
    }
    return result
}

fun main() {
    val lines = readLines(23, 5, "data.txt")
    assert(lines[0].startsWith("seeds: "))
    val seeds = parseSeeds2(lines)
    val sections = parseSections(lines)
    val locations = seeds.map { seed ->
        getMinSeedLocation(seed, sections)
    }
    val bestLocation = locations.min()
    // 59370572
    println(bestLocation)
}