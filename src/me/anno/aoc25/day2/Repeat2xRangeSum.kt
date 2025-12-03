package me.anno.aoc25.day2

import kotlin.math.min

object Repeat2xRangeSum : RangeSum() {

    override fun indexOf(value: Long): Int {
        val nextValue = nextInvalidId2x(value)
        val divisor: Long = when {
            nextValue < 10_0 -> 10
            nextValue < 100_00 -> 100
            nextValue < 1000_000 -> 1000
            nextValue < 10000_0000 -> 10000
            nextValue < 100000_00000 -> 100000
            nextValue < 1000000_000000 -> 1000000
            nextValue < 10000000_0000000 -> 10000000
            nextValue < 100000000_00000000 -> 100000000
            else -> throw NotImplementedError()
        }
        val index = (nextValue / divisor).toInt()
        check(index < divisor * 10)
        return if (nextValue == value) index else -index - 1
    }

    fun sum1ToN(n: Long): Long {
        return (n * (n + 1L)).ushr(1)
    }

    fun sumNToM(min: Long, maxExcl: Long): Long {
        if (maxExcl <= min) return 0L
        return sum1ToN(maxExcl - 1) - sum1ToN(min)
    }

    override fun prefixSum(index: Int): Long {
        val index = index.toLong()
        var sum = 0L
        var num = 1L
        while (index < num) {
            sum += sumNToM(num, min(index, num + 10)) * (num * 10 + 1)
            num *= 10
        }
        return sum
    }
}