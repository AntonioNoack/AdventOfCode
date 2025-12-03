package me.anno.aoc25.day2

abstract class RangeSum {

    abstract fun indexOf(value: Long): Int
    abstract fun prefixSum(index: Int): Long

    fun lowerBound(value: Long): Int {
        val idx = indexOf(value)
        return if (idx >= 0) idx else -idx - 1
    }

    fun upperBound(value: Long): Int {
        val idx = indexOf(value)
        return if (idx >= 0) idx + 1 else -idx - 1
    }

    /**
     * n357x.sumOf { if (it in range) it else 0L }
     * */
    fun rangeSum(r: LongRange): Long {
        val lo = lowerBound(r.first)
        val hiExclusive = upperBound(r.last)

        if (lo >= hiExclusive) return 0L
        return prefixSum(hiExclusive) - prefixSum(lo)
    }

}