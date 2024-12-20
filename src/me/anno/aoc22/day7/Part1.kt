package me.anno.aoc22.day7

import me.anno.utils.Utils.readLines

class Node(val parent: Node?) {

    var totalSize = 0L
    val children = HashMap<String, Long>()
    val childNodes = HashMap<String, Node>()

    fun getChildNode(name: String): Node {
        return childNodes.getOrPut(name) {
            Node(this)
        }
    }

    fun defineFile(name: String, size: Long) {
        val prev = children[name] ?: 0L
        children[name] = size
        val delta = size - prev
        var node = this
        while (true) {
            node.totalSize += delta
            node = node.parent ?: break
        }
    }

}

fun parseNodes(fileName: String): Node {
    val commands = readLines(22, 7, fileName)
    val root = Node(null)
    var current = root
    var i = 0
    while (i < commands.size) {
        val input = commands[i++]
        if (!input.startsWith("$ ")) throw IllegalStateException()
        fun getOutput(): List<String> {
            val output = ArrayList<String>()
            while (i < commands.size && !commands[i].startsWith("$")) {
                output.add(commands[i++])
            }
            return output
        }
        when {
            input == "$ ls" -> {
                val output = getOutput()
                for (line in output) {
                    if (line.startsWith("dir ")) continue
                    val k = line.indexOf(' ')
                    val name = line.substring(k + 1)
                    val size = line.substring(0, k).toLong()
                    current.defineFile(name, size)
                }
            }
            input == "$ cd /" -> current = root
            input == "$ cd .." -> current = current.parent!!
            input.startsWith("$ cd ") -> {
                val name = input.substring(5)
                current = current.getChildNode(name)
            }
            else -> throw IllegalArgumentException(input)
        }
    }
    return root
}

fun main() {
    var total = 0L
    val sizeLimit = 100_000
    fun checkNode(node: Node) {
        if (node.totalSize <= sizeLimit) {
            total += node.totalSize
        }
        for ((_, child) in node.childNodes) {
            checkNode(child)
        }
    }
    checkNode(parseNodes("data.txt"))
    println(total)
    // solution: 1611443
}