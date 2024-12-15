package me.anno.aoc24.day15

import me.anno.utils.Utils.findPosition1
import me.anno.utils.Utils.readLines
import me.anno.utils.Vector2i

val boxL = '['
val boxR = ']'

val expandMap = mapOf(
    box to "$boxL$boxR",
    wall to "$wall$wall",
    robot to "$robot$empty",
    empty to "$empty$empty"
)

fun expandHorizontally(line: String): CharArray {
    val dst = CharArray(line.length * 2)
    for (i in line.indices) {
        val fill = expandMap[line[i]]!!
        dst[i * 2] = fill[0]
        dst[i * 2 + 1] = fill[1]
    }
    return dst
}

fun main() {
    val lines = readLines(24, 15, "data.txt")
    val emptyLineIdx = lines.indexOf("")
    val field0 = lines.subList(0, emptyLineIdx)
    val field1 = field0.map { expandHorizontally(it) }
    val moves = lines.subList(emptyLineIdx + 1, lines.size)
        .joinToString("").mapNotNull { directions[it] }
    printField(field1)
    var robotPos = findPosition1(field1, robot)
    val dx = Vector2i(1, 0)
    for (dir in moves) {

        // try to apply move...
        // first find out how many boxes should be moved
        val newRobotPos = robotPos + dir
        val boxSide = getField(field1, newRobotPos)

        var shallMoveRobot = false
        if (boxSide == empty) { // easy
            shallMoveRobot = true
        } else if (dir.x == 0) {
            // up/down
            if (boxSide == boxL || boxSide == boxR) {
                // check all boxes, whether they can be moved
                val boxes = HashSet<Vector2i>() // position of [
                fun canMoveBox(v: Vector2i): Boolean {
                    boxes.add(v)
                    val leftSpacePos = v + dir
                    val leftSpace = getField(field1, leftSpacePos)
                    val rightSpacePos = leftSpacePos + dx
                    val rightSpace = getField(field1, rightSpacePos)
                    return when (leftSpace) {
                        wall -> false
                        boxL -> canMoveBox(leftSpacePos)
                        boxR -> canMoveBox(leftSpacePos - dx)
                        empty -> true
                        else -> throw IllegalStateException()
                    } && when (rightSpace) {
                        wall -> false
                        boxL -> canMoveBox(rightSpacePos)
                        empty, boxR -> true
                        else -> throw IllegalStateException("Unknown char $rightSpace")
                    }
                }

                if (canMoveBox(if (boxSide == boxR) newRobotPos - dx else newRobotPos)) {
                    shallMoveRobot = true
                    for (box in boxes) {
                        setField(field1, box, empty)
                        setField(field1, box + dx, empty)
                    }
                    for (box in boxes) {
                        setField(field1, box + dir, boxL)
                        setField(field1, box + dir + dx, boxR)
                    }
                }

            }// else a wall
        } else {
            // left/right
            var numHalfBoxes = 0
            while (true) {
                val char = getField(field1, robotPos + (dir * (numHalfBoxes + 1)))
                if (char == boxL || char == boxR) {
                    numHalfBoxes++
                } else break
            }
            assert(numHalfBoxes % 2 == 0)
            // then check if the field after that is empty
            val gapPos = robotPos + (dir * (numHalfBoxes + 1))
            if (getField(field1, gapPos) == empty) {
                // and if so, apply the move
                for (i in 0 until numHalfBoxes) {
                    val boxI = if ((i.and(1) == 1) xor (boxSide == boxL)) boxL else boxR
                    setField(field1, robotPos + (dir * (i + 2)), boxI)
                }
                shallMoveRobot = true
            }
            // else nothing to do
        }

        if (shallMoveRobot) {
            setField(field1, robotPos, empty)
            setField(field1, newRobotPos, robot)
            robotPos = newRobotPos
        }
    }
    printField(field1)
    println(getCoordSum(field1, boxL))
}