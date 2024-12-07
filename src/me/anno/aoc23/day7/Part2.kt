package me.anno.aoc23.day7

import me.anno.utils.Utils.readLines

fun getCardType2(hand: String, game: CardGame): CardType {
    val baseType = getCardType(hand, game)
    val numJokers = counters[getCountIndex('J', game)]
    return when (numJokers) {
        0 -> baseType
        1 -> {
            when (baseType) {
                CardType.FIVE_OF_A_KIND, CardType.FOUR_OF_A_KIND -> CardType.FIVE_OF_A_KIND
                CardType.FULL_HOUSE -> throw IllegalStateException("Impossible")
                CardType.THREE_OF_A_KIND -> CardType.FOUR_OF_A_KIND // JXXXY -> XXXXY
                CardType.TWO_PAIR -> CardType.FULL_HOUSE // JXXYY -> XXXYY
                CardType.ONE_PAIR -> CardType.THREE_OF_A_KIND // JXXYZ -> XXXYZ
                CardType.HIGH_CARD -> CardType.ONE_PAIR // JXYZW -> XXYZW
            }
        }
        2 -> {
            when (baseType) {
                CardType.FIVE_OF_A_KIND, CardType.FOUR_OF_A_KIND -> throw IllegalStateException("Impossible")
                CardType.FULL_HOUSE -> CardType.FIVE_OF_A_KIND // JJXXX -> XXXXX
                CardType.THREE_OF_A_KIND -> throw IllegalStateException("Impossible")
                CardType.TWO_PAIR -> CardType.FOUR_OF_A_KIND // JJXYY -> YYXYY
                CardType.ONE_PAIR -> CardType.THREE_OF_A_KIND // JJXYZ -> XXXYZ
                CardType.HIGH_CARD -> throw IllegalStateException("Impossible")
            }
        }
        3 -> {
            if (counters.any { it == 2 }) {
                CardType.FIVE_OF_A_KIND
            } else {
                CardType.FOUR_OF_A_KIND
            }
        }
        4, 5 -> CardType.FIVE_OF_A_KIND
        else -> throw IllegalStateException("Impossible")
    }
}

fun getStrength2(hand: String, game: CardGame): Int {
    return getStrength(getCardType2(hand, game), getHandStrength(hand, game))
}

fun main() {
    val players = readLines(23, 7, "data.txt")
        .mapNotNull { parsePlayer(it) }
    val cardGame = CardGame("A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J")
    val bets = players.map {
        Bet(it.bid, getStrength2(it.hand, cardGame))
    }
    val totalScore = bets.sorted().withIndex()
        .sumOf { (it.index + 1) * it.value.bid }
    // 248618050 -> too low??? -> we skipped that J is now the least valuable card
    // solution: 248750248
    println(totalScore)
}