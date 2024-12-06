package me.anno.aoc24.day2

import me.anno.utils.Utils.readLines

fun isReportSafeDampener(list: List<Int>): Boolean {
    return isReportSafeDampenerOneWay(list) || isReportSafeDampenerOneWay(list.reversed())
}

fun isReportSafeDampenerOneWay(list: List<Int>): Boolean {
    for (i in 1 until list.size) {
        val a = list[i - 1]
        val b = list[i]
        if (a - b !in 1..3) {
            // potentially unsafe sample -> try with dampener
            return if (i + 1 < list.size) {
                val c = list[i + 1]
                // check that connection is still good;
                // if i == 1, we could also remove the start, and then b-c is relevant instead of a-c
                if (a - c in 1..3 || (i == 1 && b - c in 1..3)) {
                    // connection is good
                    val start = i + 2
                    isReportSafeOneWay(list, start)
                } else {
                    // removing that element isn't good enough
                    false
                }
            } else {
                // is safe by removing that last element
                true
            }
        }
    }
    return true
}

fun main() {
    val lines = readLines(24, 2, "data.txt")
    val reports = lines
        .filter { it.isNotBlank() }
        .map { line -> line.split(' ').map { it.toInt() } }
    val safeReports = reports.count(::isReportSafeDampener)
    println(safeReports)
}