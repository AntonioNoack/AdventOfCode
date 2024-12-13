package me.anno.aoc23.day19

import me.anno.utils.Utils.readLines
import kotlin.math.max
import kotlin.math.min

data class Case(val ranges: List<IntRange>, val ruleSet: RuleSet)

/**
 * calculate, how many combinations will be accepted
 *  -> each branch is a split
 * */
fun main() {

    val lines = readLines(23, 19, "data.txt")
    val empty = lines.indexOf("")
    val rules = lines.subList(0, empty).map { parseRuleSet(it) } + listOf(reject, accept)
    val inRule = linkRules(rules)

    val all = 1..4000
    val remaining = ArrayList<Case>()
    remaining.add(Case(listOf(all, all, all, all), inRule))

    var acceptSum = 0L
    loop@ while (true) {
        val (ranges, ruleSet) = remaining.removeLastOrNull() ?: break
        if (ruleSet == accept) {
            acceptSum += ranges.map { it.size() }.product()
            continue@loop
        } else if (ruleSet == reject) {
            continue@loop
        }
        var remainder = ranges
        for (rule in ruleSet.rules) {
            val (ifTrue, ifFalse) = rule.check(remainder)
            if (!ifTrue.isEmpty1()) {
                remaining.add(Case(ifTrue, rule.ifTrue))
            }
            remainder = ifFalse
            if (remainder.isEmpty1()) continue@loop // skip early
        }
        // empty case would be skipped already
        remaining.add(Case(remainder, ruleSet.ifFalse))
    }

    println(acceptSum)

}

fun IntRange.size(): Long {
    return max(last + 1 - first, 0).toLong()
}

fun List<Long>.product(): Long {
    return reduce { a, b -> a * b }
}

fun Rule.check(ranges: List<IntRange>): Pair<List<IntRange>, List<IntRange>> {
    val range = ranges[case]
    val ifTrue: IntRange
    val ifFalse: IntRange
    if (lessThan) {
        ifTrue = range.first..min(number - 1, range.last)
        ifFalse = max(number, range.first)..range.last
    } else {
        // greaterThan
        ifTrue = max(number + 1, range.first)..range.last
        ifFalse = range.first..min(number, range.last)
    }
    return Pair(
        replace(ranges, case, ifTrue),
        replace(ranges, case, ifFalse)
    )
}

fun replace(ranges: List<IntRange>, case: Int, value: IntRange): List<IntRange> {
    val clone = ArrayList(ranges)
    clone[case] = value
    return clone
}

fun List<IntRange>.isEmpty1(): Boolean {
    return all { it.isEmpty() }
}
