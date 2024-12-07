package me.anno.aoc23.day7

import me.anno.utils.Utils.readLines

fun getHandStrength(hand: String, cardGame: CardGame): Int {
    assert(hand.length == 5)
    var sum = 0
    val cardTypes = cardGame.cardTypes
    for (card in hand) {
        sum = sum * cardTypes.size + cardTypes[card]!!
    }
    return sum
}

fun getCountIndex(card: Char, cardGame: CardGame): Int {
    return cardGame.cardTypes[card]!!
}

val counters = IntArray(16)
fun getCardType(hand: String, cardGame: CardGame): CardType {
    assert(hand.length == 5)
    counters.fill(0)
    for (card in hand) {
        counters[getCountIndex(card, cardGame)]++
    }
    val maxSame = counters.max()
    return when (maxSame) {
        5 -> CardType.FIVE_OF_A_KIND
        4 -> CardType.FOUR_OF_A_KIND
        3 -> {
            if (counters.any { it == 2 }) {
                CardType.FULL_HOUSE
            } else {
                CardType.THREE_OF_A_KIND
            }
        }
        2 -> {
            if (counters.count { it == 2 } == 2) {
                CardType.TWO_PAIR
            } else {
                CardType.ONE_PAIR
            }
        }
        else -> CardType.HIGH_CARD
    }
}

fun parsePlayer(line: String): Player? {
    if (line.length < 7) return null
    val hand = line.substring(0, 5)
    val bid = line.substring(6).toInt()
    return Player(hand, bid)
}

fun getStrength(type: CardType, handStrength: Int): Int {
    val byType = CardType.entries.size - type.ordinal
    return byType.shl(20) + handStrength
}

fun getStrength(hand: String, game: CardGame): Int {
    return getStrength(getCardType(hand, game), getHandStrength(hand, game))
}

fun main() {
    val players = readLines(23, 7, "data.txt")
        .mapNotNull { parsePlayer(it) }
    val cardGame = CardGame("A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2")
    val bets = players.map {
        Bet(it.bid, getStrength(it.hand, cardGame))
    }
    val totalScore = bets.sorted().withIndex()
        .sumOf { (it.index + 1) * it.value.bid }
    // solution: 249390788
    println(totalScore)
}