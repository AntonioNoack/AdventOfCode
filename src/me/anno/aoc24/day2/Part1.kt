package me.anno.aoc24.day2

import me.anno.aoc24.utils.Utils.readLines

fun isReportSafe(list: List<Int>): Boolean {
    return isReportSafeOneWay(list) || isReportSafeOneWay(list.reversed())
}

fun isReportSafeOneWay(list: List<Int>, start: Int = 1): Boolean {
    for (i in start until list.size) {
        val a = list[i - 1]
        val b = list[i]
        if (a - b !in 1..3) {
            return false
        }
    }
    return true
}

fun main() {
    val lines = readLines(2, "data.txt")
    val reports = lines
        .filter { it.isNotBlank() }
        .map { line -> line.split(' ').map { it.toInt() } }
    val safeReports = reports.count(::isReportSafe)
    println(safeReports)
}