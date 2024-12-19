package me.anno.aoc24.day19

import me.anno.utils.Maths.hasFlag
import kotlin.math.min

data class CompactStripe(val stripe: String) {
    val times = HashMap<Long, Long>()
}

fun main() {
    val (available, targets) = parse("data.txt")
    val targetsJoined = targets.joinToString(",")
    // remove all stripes, that aren't used anyway
    val available1 = available.filter { it in targetsJoined }
    // optimize access
    val byFirstThree = available1
        .sortedByDescending { it.length } // longest first
        .map { stripe -> CompactStripe(stripe) }
        // .onEach { println("pattern: $it") }
        .groupBy { (stripe) ->
            stripe.substring(0, min(stripe.length, 3))
        }
    println("optimized stripes: ${available.size} -> ${available1.size}")
    val t0 = System.nanoTime()
    val sumPossible = targets.sumOf { target ->
        val r = countPossiblePatterns(byFirstThree, target)
        if (target.length < 20) {
            val x = countPossiblePatternsTrivial(byFirstThree, target, 0, 0L)
            println("$target: $r, vs $x")
            if (r != x) throw IllegalStateException()
        }
        r
    }
    println("$sumPossible, ${(System.nanoTime() - t0) / 1e9f}s")
    // solution:           198600 -> too low, is also lower than I'd have expected...
    //              3823472706024 -> too low, too? :(
    //           2127717408716768 -> too high... my bit magic is probably incorrect somewhere...
    //           1041529704688380 -> correct 🤩
}

var print = false

fun countPossiblePatterns(available: Map<String, List<CompactStripe>>, target: String): Long {
    if (print) println("\nChecking pattern $target\n")
    return countPossiblePatternsCached(available, target, 0, 0L, HashMap())
}

data class CacheKey(val ti: Int, val forbiddenTis: Long)

fun countPossiblePatternsCached(
    available: Map<String, List<CompactStripe>>, target: String,
    ti: Int, forbiddenTis0: Long,
    cache: HashMap<CacheKey, Long>,
): Long {
    val relevantBits = forbiddenTis0 ushr ti
    return cache.getOrPut(CacheKey(ti, relevantBits)) {
        countPossiblePatterns1(available, target, ti, forbiddenTis0, cache)
    }
}

fun countPossiblePatterns1(
    available: Map<String, List<CompactStripe>>, target: String,
    ti: Int, forbiddenTis0: Long,
    cache: HashMap<CacheKey, Long>,
): Long {
    if (target.length == ti) return 1L
    var sum = 0L
    var forbiddenTis = forbiddenTis0
    for (step in 3 downTo 1) {
        if (ti + step > target.length) continue

        val key = target.substring(ti, ti + step)
        val options = available[key] ?: continue
        for (stripe0 in options) {
            val stripe = stripe0.stripe
            val ti2 = ti + stripe.length
            if (ti2 <= target.length &&
                !forbiddenTis.hasFlag(1L shl (ti2 - 1)) &&
                target.startsWith(stripe, ti)
            ) {

                if (print) {
                    println("($ti2) was blocked by ($ti)")
                    println(" ".repeat(ti) + "[$stripe]")
                }
                forbiddenTis += 1L shl (ti2 - 1)

                val count = countPossiblePatternsCached(available, target, ti2, forbiddenTis, cache)
                if (count > 0) {
                    val timesKey = getTimesKey(forbiddenTis, ti, ti2)
                    val multiplier = stripe0.times.getOrPut(timesKey) {
                        countPossiblePatternsTrivial(available, stripe, 0, timesKey)
                    }
                    if (print) {
                        println(" ".repeat(ti) + "[$stripe] $sum += $multiplier[$timesKey] * $count @ $ti")
                    }
                    sum += multiplier * count
                    if (print) println(" ".repeat(ti) + "[$stripe] -> $sum")
                }
            }
        }
    }
    return sum
}

fun getTimesKey(forbiddenTis: Long, ti0: Int, ti1: Int): Long {
    return getTimesKey0(forbiddenTis, ti0, ti1 - 1)
}

fun getTimesKey0(forbiddenTis: Long, ti0: Int, ti1: Int): Long {
    val length = ti1 - ti0
    val mask = (1L shl length) - 1L
    return forbiddenTis.ushr(ti0).and(mask)
}

fun countPossiblePatternsTrivial(
    available: Map<String, List<CompactStripe>>,
    target: String, ti: Int,
    forbiddenTis: Long
): Long {
    if (target.length == ti) return 1L
    var sum = 0L
    for (step in 1..3) {
        if (ti + step > target.length) break
        val key = target.substring(ti, ti + step)
        val options = available[key] ?: continue
        for ((stripe) in options) {
            val ti2 = ti + stripe.length
            val flag = 1L shl (ti2 - 1)
            if (ti2 <= target.length && !forbiddenTis.hasFlag(flag) && target.startsWith(stripe, ti)) {
                sum += countPossiblePatternsTrivial(available, target, ti2, forbiddenTis)
            }
        }
    }
    return sum
}
