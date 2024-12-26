package me.anno.aoc24.day22

import me.anno.utils.Utils.readLines

fun main() {
    val initialSecrets = readLines(24, 22, "data.txt")
        .map { it.toInt() }
    for (sec in initialSecrets) {
        indexSequence(sec)
    }
    val (bestSequence, bestScore) = bestSequences.maxBy { it.value }
    println("$bestSequence: ${bestScore}x")
    // solution: [-1, -1, 2, 1]: 1791x
}

val bestSequences = HashMap<List<Int>, Int>()

fun indexSequence(sec0: Int) {
    val doneSequences = HashSet<List<Int>>()
    var currSec = sec0
    var sequence = listOf(-10, -10, -10, -10)
    for (i in 0 until 2000) {

        val prevSec = currSec
        currSec = nextSecret(currSec)

        val change = getChange(prevSec, currSec)
        sequence = nextSequence(sequence, change)

        if (i >= 3) { // valid sequence identifier
            if (doneSequences.add(sequence)) {
                bestSequences[sequence] = (bestSequences[sequence] ?: 0) + getPrize(currSec)
            }
        }
    }
}

fun getPrize(secret: Int): Int {
    return (secret % 10)
}

fun getChange(prevSecret: Int, currSecret: Int): Int {
    return getPrize(currSecret) - getPrize(prevSecret)
}

fun nextSequence(prevSequence: List<Int>, change: Int): List<Int> {
    if (change !in -9..9) throw IllegalStateException()
    return prevSequence.subList(1, 4) + change
}
