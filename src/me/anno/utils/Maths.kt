package me.anno.utils

object Maths {
    fun posMod(a: Int, b: Int): Int {
        val r = a % b
        return if (r < 0) r + b else r
    }

    fun Int.hasFlag(flag: Int): Boolean {
        return this.and(flag) == flag
    }

    fun Long.hasFlag(flag: Long): Boolean {
        return this.and(flag) == flag
    }
}