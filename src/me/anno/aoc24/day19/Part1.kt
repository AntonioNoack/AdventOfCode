package me.anno.aoc24.day19

import me.anno.utils.Utils.readLines
import kotlin.math.min

fun isCombination(stripe: String, begin: String, available1s: Set<String>): Boolean {
    return stripe.startsWith(begin) && stripe.substring(begin.length) in available1s
}

fun parse(name: String): Pair<List<String>,List<String>> {
    val lines = readLines(24, 19, "data.txt")
    val available = lines[0].split(", ")
        .sortedBy { it.length }
    val targets = lines.subList(2, lines.size)
    return available to targets
}

fun main() {
    val (available, targets) = parse("data.txt")
    val targetsJoined = targets.joinToString(",")
    // remove all stripes, that aren't used anyway
    val available1 = available.filter { it in targetsJoined }
    val available1s = available1.toHashSet()
    // remove all stripes, that are a combination of two others
    val available2 = available1.filter { stripe ->
        available1.none { begin -> isCombination(stripe, begin, available1s) }
    }
    // optimize access
    val byFirstThree = available2.groupBy {
        it.substring(0, min(it.length, 3))
    }
    println("optimized stripes: ${available.size} -> ${available1.size} -> ${available2.size}")
    val numPossible = targets.count { target ->
        val r = isPossiblePattern(byFirstThree, target)
        println("$target: $r")
        r
    }
    println(numPossible)
    // solution: 219 -> too low, was forgetting to check for step=2 and step=1
    //           272 -> correct :)
}

fun isPossiblePattern(available: Map<String, List<String>>, target: String): Boolean {
    return isPossiblePattern(available, target, 0)
}

fun isPossiblePattern(available: Map<String, List<String>>, target: String, ti: Int): Boolean {
    if (isPossiblePattern(available, target, ti, 3)) return true
    if (isPossiblePattern(available, target, ti, 2)) return true
    if (isPossiblePattern(available, target, ti, 1)) return true
    return false
}

fun isPossiblePattern(available: Map<String, List<String>>, target: String, ti: Int, step: Int): Boolean {
    if (ti + step > target.length) return false
    val key = target.substring(ti, min(ti + step, target.length))
    val options = available[key] ?: return false
    for (stripe in options) {
        if (target.startsWith(stripe, ti)) {
            if (isPossiblePattern(available, target, ti + stripe.length)) {
                return true
            }
        }
    }
    return false
}