package me.anno.aoc24.day9

import me.anno.utils.Utils.readText

data class DiskMapEntry(val length: Int, var space: Int, val id: Int)

fun main() {
    // length, space, length, space, length, ...
    val diskMap = readText(24, 9, "data.txt")
        .filter { it in '0'..'9' }.map { it - '0' } + listOf(0)
    val diskMap1 = (0 until diskMap.size.shr(1)).map {
        DiskMapEntry(diskMap[it * 2], diskMap[it * 2 + 1], it)
    }.toMutableList()
    compactMovingFilesOnce(diskMap1)
    // solution: 6272188244509
    println(checksumOfDiskMap(diskMap1))
}

fun checksumOfDiskMap(diskMap: List<DiskMapEntry>): Long {
    var sum = 0L
    var diskI = -1
    for (entry in diskMap) {
        val diskI0 = diskI
        diskI += entry.length
        sum += sumOfItoJ(diskI0, diskI) * entry.id
        // print("${entry.id}".repeat(entry.length))
        // print(".".repeat(entry.space))
        diskI += entry.space
    }
    // println()
    return sum
}

fun compactMovingFilesOnce(diskMap: MutableList<DiskMapEntry>) {
    var cursor = diskMap.lastIndex
    while (cursor > 0) {
        val movedEntry = diskMap[cursor]
        val moveLength = movedEntry.length
        val moveGap = movedEntry.space
        // check if we can insert that file somehow
        val gap = findFirstGap(diskMap, moveLength, cursor)
        // println("Moving $cursor into $gap")
        if (gap == cursor - 1) {
            // file is moved forward a bit
            val gapEntry = diskMap[gap]
            movedEntry.space += gapEntry.space
            gapEntry.space = 0
            // move cursor to gapEntry, because movedEntry hasn't changed its index
            cursor--
        } else if (gap >= 0) {
            // file can be moved to the front
            val gapEntry = diskMap[gap]
            movedEntry.space = gapEntry.space - moveLength
            gapEntry.space = 0
            diskMap[cursor - 1].space += moveLength + moveGap // space before file is increased
            diskMap.removeAt(cursor)
            diskMap.add(gap + 1, movedEntry)
            // cursor doesn't change, because we moved the file to the front
        } else {
            // file cannot be moved -> update cursor to next file
            cursor--
        }
    }
}

fun findFirstGap(diskMap: List<DiskMapEntry>, size: Int, cursor: Int): Int {
    for (i in 0 until cursor) {
        if (diskMap[i].space >= size) {
            return i
        }
    }
    return -1
}