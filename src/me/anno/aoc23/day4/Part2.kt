package me.anno.aoc23.day4

import me.anno.utils.Utils.readLines

fun main() {
    val cards = readLines(23, 4, "data.txt")
        .map { parseCard(it) }
    val cardCounter = LongArray(cards.size) { 1 }
    for ((i, card) in cards.withIndex()) {
        val score = numWinning(card)
        val mx = cardCounter[i] // this many cards
        for (j in 0 until score) {
            cardCounter[i + j + 1] += mx
        }
    }
    println(cardCounter.sum())
    // solution: 9496801
}