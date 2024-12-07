package me.anno.aoc23.day7

class Bet(val bid: Int, private val strength: Int) : Comparable<Bet> {
    override fun compareTo(other: Bet): Int {
        return strength.compareTo(other.strength)
    }
}