package me.anno.aoc24.day5

import me.anno.utils.Utils.readLines

fun dependsOn(src: Int, dst: Int, dependencies: Map<Int, List<Int>>): Boolean {
    val checked = HashSet<Int>()
    val remaining = ArrayList<Int>()
    remaining.add(src)
    checked.add(src)
    while (remaining.isNotEmpty()) {
        val srcI = remaining.removeLast()
        val dependencyList = dependencies[srcI] ?: continue
        if (dst in dependencyList) return true
        for (di in dependencyList) {
            if (checked.add(di)) {
                remaining.add(di)
            }
        }
    }
    return false
}

fun fixUpdateOrder(neededPages: List<Int>, dependencies0: Map<Int, List<Int>>): List<Int> {
    val dependencies = relevantDependencies(neededPages.toHashSet(), dependencies0)
    val solution = neededPages.sortedWith { a, b ->
        if (a == b) 0 // same -> 0
        else if (dependsOn(a, b, dependencies)) +1 // must be after
        else if (dependsOn(b, a, dependencies)) -1 // must be before
        else 0 // order doesn't matter
    }

    // check that the solution is correct
    assert(fulfillsDependencies(solution, dependencies0))
    return solution
}

fun main() {
    val lines = readLines(24, 5, "data.txt")
    val dependencies = getDependencies(lines)
    val updates = getUpdates(lines)
    val invalidUpdates = updates
        .filter { !fulfillsDependencies(it, dependencies) }
    val fixedUpdates = invalidUpdates
        .map { fixUpdateOrder(it, dependencies) }
    val middleNumbersSum = middleNumbersSum(fixedUpdates)
    println("middle-fixed: $middleNumbersSum")
}