package me.anno.aoc24.day19

/**
 * my first implementation is total overkill XD,
 * let's simplify this as much as possible, and still in ~60ms (3x slower, 5x fewer lines of code)
 * */
fun main() {
    val (available, targets) = parse("data.txt")
    val t0 = System.nanoTime()
    val sumPossible = targets.sumOf { target ->
        countPossiblePatternsCachedV2(available, target, 0, HashMap())
    }
    println("$sumPossible, ${(System.nanoTime() - t0) / 1e9f}s")
}

fun countPossiblePatternsCachedV2(available: List<String>, target: String, ti: Int, cache: HashMap<Int, Long>) =
    cache.getOrPut(ti) { countPossiblePatternsV2(available, target, ti, cache) }

fun countPossiblePatternsV2(available: List<String>, target: String, ti: Int, cache: HashMap<Int, Long>): Long =
    if (ti < target.length) {
        available.sumOf { stripe ->
            if (target.startsWith(stripe, ti)) {
                countPossiblePatternsCachedV2(available, target, ti + stripe.length, cache)
            } else 0L
        }
    } else 1L
