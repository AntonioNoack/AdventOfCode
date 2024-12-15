package me.anno.aoc23.day22

import me.anno.utils.Utils.desktop
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i
import me.anno.utils.Vector3i
import java.io.File

fun main() {

    // z is up...
    val bricks = readLines(23, 22, "data.txt").map { parseBrick(it) }
    val (dependencies, invDependencies) = findDependencies(bricks)

    letBricksFall(bricks, dependencies)

    // check if removing a brick would let the others fall...
    val singleSupportBricks = findSingleSupportBricks(dependencies)
    val numCouldBeRemoved = bricks.count { lower ->
        canRemoveWithoutAnythingFalling(lower, invDependencies, singleSupportBricks)
    }

    writeBricks(bricks, "Fallen") { lower ->
        if (canRemoveWithoutAnythingFalling(lower, invDependencies, singleSupportBricks)) 0xff0000 else -1
    }

    // 337 -> too low :(
    // 47 -> not correct either, ofc...
    // we forgot to filter whether lower supports upper in canRemoveWithoutAnythingFalling
    // 507 -> correct :)
    println(numCouldBeRemoved)
}

fun letBricksFall(bricks: List<Brick>, dependencies: Map<Brick, Collection<Brick>>) {
    val fallOrder = bricks.sortedBy { it.min.z }
    val minZ = 0
    for (upper in fallOrder) {
        // let brick fall as much as possible
        val maxFallZ = dependencies[upper]
            ?.minOfOrNull { under -> upper.min.z - (under.max.z + 1) }
            ?: (upper.min.z - (minZ + 1))
        if (maxFallZ < 0) throw IllegalStateException()
        if (maxFallZ > 0) {
            // println("$upper falls $maxFallZ")
            upper.min.z -= maxFallZ
            upper.max.z -= maxFallZ
            // validate all bricks
            // apparently works... works when going single steps, too
            validateBricks(bricks)
        }
    }
}

/**
 * find all bricks, which are only supported by a single other brick
 * */
fun findSingleSupportBricks(dependencies: Map<Brick, Collection<Brick>>): Set<Brick> {
    return findSupportBricks(dependencies).filter { it.value.size == 1 }.keys
}

fun findSupportBricks(dependencies: Map<Brick, Collection<Brick>>): Map<Brick, Collection<Brick>> {
    return dependencies.mapValues { (upper, lowerBricks) ->
        lowerBricks.filter { lower -> isSupporting(lower, upper) }
    }
}

fun canRemoveWithoutAnythingFalling(
    lower: Brick,
    invDependencies: Map<Brick, Collection<Brick>>,
    singleSupportBricks: Set<Brick>
): Boolean {
    val uppers = invDependencies[lower] ?: emptyList()
    return uppers.none { upper -> fallsIfRemoved(lower, upper, singleSupportBricks) }
}

fun fallsIfRemoved(lower: Brick, upper: Brick, singleSupportBricks: Set<Brick>): Boolean {
    return isSupporting(lower, upper) && upper in singleSupportBricks
}

fun validateBricks(bricks: List<Brick>) {
    val space = HashSet<Vector3i>()
    for (brick in bricks) {
        for (z in brick.min.z..brick.max.z) {
            for (y in brick.min.y..brick.max.y) {
                for (x in brick.min.x..brick.max.x) {
                    val key = Vector3i(x, y, z)
                    if (!space.add(key)) {
                        throw IllegalStateException("Two bricks at $key")
                    }
                }
            }
        }
    }
}

fun isSupporting(lower: Brick, upper: Brick): Boolean {
    if (lower.max.z > upper.min.z) throw IllegalStateException()
    return lower.max.z + 1 == upper.min.z
}

fun findDependencies(bricks: List<Brick>): Pair<Map<Brick, Collection<Brick>>, Map<Brick, Collection<Brick>>> {
    val coordToBrick = HashMap<Vector2i, Brick>()
    val dependencyMap = HashMap<Brick, List<Brick>>()
    val invDependencies = HashMap<Brick, ArrayList<Brick>>()
    for (upper in bricks.sortedBy { it.min.z }) {
        val (min, max) = upper
        val lowerBricks = HashSet<Brick>()
        for (y in min.y..max.y) {
            for (x in min.x..max.x) {
                val v = Vector2i(x, y)
                val lower = coordToBrick[v]
                if (lower != null) {
                    if (lower.max.z >= upper.min.z) {
                        throw IllegalStateException("Invalid dependency @$v, $upper > $lower")
                    }
                    lowerBricks += lower
                }
                coordToBrick[v] = upper
            }
        }
        for (lower in lowerBricks) {
            invDependencies.getOrPut(lower, ::ArrayList).add(upper)
        }
        dependencyMap[upper] = lowerBricks.toList()
    }
    return dependencyMap to invDependencies
}

fun parseBrick(line: String): Brick {
    val parts = line.split(',', '~').map { it.toInt() }
    assert(parts.size == 6)
    val a = Vector3i(parts[0], parts[1], parts[2])
    val b = Vector3i(parts[3], parts[4], parts[5])
    return Brick(a.min(b), a.max(b))
}

// must not be a data-class, because we change it while it is used as a key of a hashmap
class Brick(val min: Vector3i, val max: Vector3i) {
    operator fun component1() = min
    operator fun component2() = max
    override fun toString(): String {
        return "[$min~$max]"
    }
}

fun writeBricks(bricks: List<Brick>, name: String, getColor: (Brick) -> Int) {
    val writer = ObjWriter("$name.mtl")
    for (brick in bricks) {
        writer.append(brick, getColor(brick))
    }
    File(desktop, "$name.obj").writeText(writer.obj.toString())
    File(desktop, "$name.mtl").writeText(writer.mtl.toString())
}
