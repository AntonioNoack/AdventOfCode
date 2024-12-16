package me.anno.aoc23.day23

import me.anno.utils.Utils.directions
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import kotlin.math.max

val slopes = "^>v<"
val path = '.'
val wall = '#'

fun main() {
    val field = readLines(23, 23, "data.txt")
    println(findLongestPathByField(field))
}

data class Link(val other: Int, val distance: Int)

val startIndex = 0
val endIndex = 1

// todo find the longest path from top to bottom
// todo never step onto the same tile twice
//  -> find all intersections, link them, store whether they were visited
fun findLongestPathByField(field: List<String>): Int {

    val start = Vector2i(field[0].indexOf('.'), 0)
    val end = Vector2i(field.last().indexOf('.'), field.lastIndex)

    val sx = field[0].length
    val sy = field.size

    val intersectionIndex = HashMap<Vector2i, Int>()
    intersectionIndex[start] = startIndex
    intersectionIndex[end] = endIndex

    val links = HashMap<Vector2i, ArrayList<Vector2i>>()
    for (y in 1 until sy - 1) {
        for (x in 1 until sx - 1) {
            if (field[y][x] != wall) {
                val pos = Vector2i(x, y)
                var numPaths = 0
                for (di in directions.indices) {
                    val dir = directions[di]
                    val symbol = field[y + dir.y][x + dir.x]
                    if (symbol != wall) {
                        // only add link, if path is indeed walkable
                        if (symbol == path || slopes[di] == symbol) {
                            links.getOrPut(pos) { ArrayList(4) }.add(pos + dir)
                        }
                        numPaths++
                    }
                }
                if (numPaths > 2) {
                    intersectionIndex[pos] = intersectionIndex.size
                }
            }
        }
    }

    // for start, end, and all intersections, register all paths
    val intersections = ArrayList<List<Link>>(intersectionIndex.size)
    for (i in 0 until intersectionIndex.size) intersections.add(emptyList())
    for ((pos, i) in intersectionIndex.entries.sortedBy { it.value }) {
        intersections[i] = findLinks(pos, links, intersectionIndex)
    }

    return findLongestPathByIntersections(intersections)
}

fun findLongestPathByIntersections(intersections: List<List<Link>>): Int {
    val pos = 0
    val visited = ArrayList<Boolean>()
    for (i in intersections.indices) visited.add(false)
    return findLongestPathRecursively(intersections, pos, visited)
}

fun findLongestPathRecursively(intersections: List<List<Link>>, pos: Int, visited: ArrayList<Boolean>): Int {

    if (pos == endIndex) {
        return 0
    }

    var longestPath = -1
    visited[pos] = true

    for ((other, distance) in intersections[pos]) {
        if (!visited[other]) { // not yet visited -> try it out
            val subDistance = findLongestPathRecursively(intersections, other, visited)
            if (subDistance >= 0) {
                val distance1 = distance + subDistance
                longestPath = max(longestPath, distance1)
            }
        }
    }

    visited[pos] = false
    return longestPath
}

fun findLinks(
    pos: Vector2i,
    links: Map<Vector2i, List<Vector2i>>,
    intersections: Map<Vector2i, Int>
): List<Link> {
    return directions.mapNotNull { dir ->
        findLinks(pos, dir, links, intersections)
    }
}

fun findLinks(
    pos0: Vector2i, dir: Vector2i,
    links: Map<Vector2i, List<Vector2i>>,
    intersections: Map<Vector2i, Int>
): Link? {
    var p0 = pos0
    var p1 = pos0 + dir
    var distance = 1
    while (true) {

        val idx = intersections[p1]
        if (idx != null) {
            return Link(idx, distance)
        }

        var p2: Vector2i? = null
        for (link in links[p1] ?: return null) {
            if (link != p0) {
                p2 = link
                break
            }
        }

        p2 ?: return null
        p0 = p1
        p1 = p2

        distance++
    }
}