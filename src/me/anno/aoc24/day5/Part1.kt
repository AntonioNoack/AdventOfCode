package me.anno.aoc24.day5

import me.anno.utils.Utils.readLines

fun relevantDependencies(neededPages: Set<Int>, dependencies: Map<Int, List<Int>>): Map<Int, List<Int>> {
    return dependencies
        .filter { (key, _) -> key in neededPages }
        .mapValues { (_, value) -> value.filter { it in neededPages } }
        .filter { (_, value) -> value.isNotEmpty() }
}

fun fulfillsDependencies(neededPages: List<Int>, dependencies0: Map<Int, List<Int>>): Boolean {
    val remaining = neededPages.toHashSet()
    val dependencies = relevantDependencies(remaining, dependencies0)
    val done = HashSet<Int>()
    val needsChecks = ArrayList<Int>()
    for (page in neededPages) {
        remaining.remove(page)
        // check that all dependencies can be added
        //  that is true, if they aren't part of remaining
        needsChecks.add(page)
        while (needsChecks.isNotEmpty()) {
            val pageI = needsChecks.removeLast()
            // only rules, which are part of neededPages, are checked
            if (pageI in remaining) {
                // println("Invalid$neededPages, because $page -> $pageI")
                return false // invalid
            }
            if (done.add(pageI)) {
                // needs to be processed
                val dependencyList = dependencies[pageI]
                if (dependencyList != null) {
                    needsChecks.addAll(dependencyList)
                }
            }
        }
    }
    return true
}

fun getDependencies(lines: List<String>): Map<Int, List<Int>> {
    return lines
        .filter { '|' in it }
        .groupBy({
            val splitIndex = it.indexOf('|')
            it.substring(splitIndex + 1).toInt()
        }, {
            val splitIndex = it.indexOf('|')
            it.substring(0, splitIndex).toInt()
        })
}

fun getUpdates(lines: List<String>): List<List<Int>> {
    return lines
        .filter { '|' !in it && it.isNotBlank() }
        .map { line -> line.split(',').map { it.toInt() } }
}

fun middleNumbersSum(lines: List<List<Int>>): Int {
    return lines.sumOf {
        it[it.size / 2]
    }
}

fun main() {
    val lines = readLines(24, 5, "data.txt")
    val dependencies = getDependencies(lines)
    val updates = getUpdates(lines)
    val validUpdates = updates
        .filter { fulfillsDependencies(it, dependencies) }
    // println("valid updates: $validUpdates")
    val middleNumbersSum = middleNumbersSum(validUpdates)
    println("middle-valid: $middleNumbersSum")
}