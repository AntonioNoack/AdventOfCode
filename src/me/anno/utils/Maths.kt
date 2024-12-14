package me.anno.utils

object Maths {
    fun posMod(a: Int, b: Int): Int {
        val r = a % b
        return if (r < 0) r + b else r
    }
}