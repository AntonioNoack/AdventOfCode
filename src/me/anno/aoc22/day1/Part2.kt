package me.anno.aoc22.day1

fun main() {
    val rations = readRations()
    val sums = rations.map { it.sum() }
    val largest3 = sums.sortedDescending().subList(0, 3)
    println("$largest3 -> ${largest3.sum()}")
}