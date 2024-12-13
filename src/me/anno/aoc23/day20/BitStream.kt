package me.anno.aoc23.day20

class BitStream {
    val sequence = StringBuilder()
    fun add(bit: Boolean) {
        sequence.append(if (bit) '1' else '0')
    }

    override fun toString(): String {
        return sequence.toString()
    }
}