package me.anno.aoc25.day2

val n357x =
    (n3x + n5x + n7x)
        .sorted()
        .distinct()
        .filter { nextInvalidId2x(it) != it }
        .toLongArray()

val n357xPrefix = run {
    println("n357x has ${n357x.size} values") // ~1M -> ~8MiB XD
    val arr = n357x// already sorted
    val prefix = LongArray(arr.size + 1)
    for (i in arr.indices) {
        prefix[i + 1] = prefix[i] + arr[i]
    }
    prefix
}

fun sumInvalidIdsNxV2(range: LongRange): Long {
    return sumInvalidIds2x(range) +
            // n357x.sumOf { if (it in range) it else 0L }
            rangeSum(n357x, n357xPrefix, range)
}

fun LongArray.lowerBound(value: Long): Int {
    val idx = this.binarySearch(value)
    return if (idx >= 0) idx else -idx - 1
}

fun LongArray.upperBound(value: Long): Int {
    val idx = this.binarySearch(value)
    return if (idx >= 0) idx + 1 else -idx - 1
}

fun rangeSum(arr: LongArray, prefix: LongArray, r: LongRange): Long {
    val lo = arr.lowerBound(r.first)
    val hiExclusive = arr.upperBound(r.last)

    if (lo >= hiExclusive) return 0L
    return prefix[hiExclusive] - prefix[lo]
}

fun correctnessCheck() {
    for (i in 100L until 500) {
        for (j in i until 500) {
            val expected = sumInvalidIdsNx(i until j)
            val actual = sumInvalidIdsNxV2(i until j)
            check(expected == actual) {
                "$i,$j -> $expected vs $actual"
            }
        }
    }
}

fun main() {
    correctnessCheck()
    println("Correctness validated")

    // https://www.youtube.com/watch?v=txlEVv4sf0A
    // -> for this Zig user, it takes one second, for me 28 ms 😄
    // but ours could be optimized lots, if we just use an iterator
    // -> yes, we're now down to 0.024 ms excluding preparation 😁
    val t0 = System.nanoTime()
    val runs = 10_000
    var sum = 0L
    repeat(runs) {
        // I hope this loop doesn't get optimized out ^^
        sum += sumInvalidIdsNxV2(0L until 127554432L)
    }
    sum /= runs
    val t1 = System.nanoTime()
    println("Sum: $sum, ${(t1 - t0) / (1e6f * runs)} ms each for $runs runs (${(t1 - t0) / 1e6f} ms total)")
}