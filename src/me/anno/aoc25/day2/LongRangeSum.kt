package me.anno.aoc25.day2

class LongRangeSum(val sorted: LongArray, val prefixSum: LongArray) : RangeSum() {
    override fun indexOf(value: Long): Int = sorted.binarySearch(value)
    override fun prefixSum(index: Int): Long = prefixSum[index]
}