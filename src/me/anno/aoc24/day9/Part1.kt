package me.anno.aoc24.day9

import me.anno.utils.Utils.readText
import kotlin.math.min

fun main() {
    // length, space, length, space, length, ...
    val diskMap = readText(24, 9, "data.txt")
        .filter { it in '0'..'9' }.map { it - '0' }
    assert(diskMap.size % 2 == 1)
    // solution: 6242766523059
    println(checksumOfDiskMap(diskMap.toMutableList()))
}

fun checksumOfDiskMap(diskMap: MutableList<Int>): Long {
    var readI = 0
    var readJ = diskMap.lastIndex
    var sum = 0L
    var diskI = -1 // 1 offset for sum-of-calculation

    fun addSequence(length: Int, id: Int) {
        val diskI0 = diskI
        diskI += length
        sum += sumOfItoJ(diskI0, diskI) * id
    }

    while (readI < readJ) {
        val filledId = readI shr 1
        val filled = diskMap[readI++]
        var space = diskMap[readI++]
        addSequence(filled, filledId)
        while (space > 0) {
            val lastId = readJ shr 1
            val filled1 = diskMap[readJ]
            val numRead = min(space, filled1)
            if (space >= filled1) {
                // skip that entry and corresponding space
                readJ -= 2
                space -= filled1
            } else {
                diskMap[readJ] -= space
                space = 0
            }
            addSequence(numRead, lastId)
        }
    }
    // handle last case
    val filledId = readI shr 1
    val filled = diskMap[readI]
    addSequence(filled, filledId)
    return sum
}

fun sumOf1toN(n: Int): Long {
    return (n * (n + 1L)).shr(1)
}

fun sumOfItoJ(i: Int, j: Int): Long {
    return sumOf1toN(j) - sumOf1toN(i)
}