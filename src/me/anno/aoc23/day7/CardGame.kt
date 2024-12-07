package me.anno.aoc23.day7

class CardGame(cardTypes: String) {

    // Map<Card: Char, Strength: Int>
    val cardTypes = cardTypes.split(", ")
        .map { it[0] }.reversed()
        .withIndex().associate { it.value to it.index }

}