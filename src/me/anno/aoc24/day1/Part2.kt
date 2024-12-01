package me.anno.aoc24.day1

fun main() {
    val numbers = numbers()
    val howOften = HashMap<Int, Int>()
    for (i in numbers.map { it.second }) {
        howOften[i] = (howOften[i] ?: 0) + 1
    }
    val score = numbers.map { it.first }.sumOf {
        it * (howOften[it] ?: 0)
    }
    println(score)
}