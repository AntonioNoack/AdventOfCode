package me.anno.aoc22.day7

import kotlin.math.min


fun main() {
    val root = parseNodes("data.txt")
    val totalSpace = 70_000_000L
    val requiredSpace = 30_000_000L
    val freeSpace = totalSpace - root.totalSize
    val neededSpace = requiredSpace - freeSpace
    var bestSize = Long.MAX_VALUE
    fun checkNode(node: Node) {
        if (node.totalSize >= neededSpace) {
            bestSize = min(bestSize, node.totalSize)
        }
        for ((_, child) in node.childNodes) {
            checkNode(child)
        }
    }
    checkNode(root)
    println(bestSize)
    // solution: 13431333 -> too high??... was calculating the requirements wrong
    //            2086088 -> correct :)
}