package me.anno.aoc25.day2

val repeat357xRangeSum = run {
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
    LongRangeSum(n357x, n357xPrefix)
}

fun sumInvalidIdsNxV2(range: LongRange): Long {
    return Repeat2xRangeSum.rangeSum(range) + repeat357xRangeSum.rangeSum(range)
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
    // -> and now we're down to 43 ns 😂
    val t0 = System.nanoTime()
    val runs = 1000_000
    var sum = 0L
    repeat(runs) {
        // I hope this loop doesn't get optimized out ^^
        sum += sumInvalidIdsNxV2(0L until 127554432L)
    }
    sum /= runs
    val t1 = System.nanoTime()
    println("Sum: $sum, ${(t1 - t0) / runs} ns each for $runs runs (${(t1 - t0) / 1e6f} ms total)")
}