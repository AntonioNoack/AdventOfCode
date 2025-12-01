package me.anno.aoc22.day11

import me.anno.utils.Utils.readLines

// todo why/how was this broken -> ahh :), the answer depends on each user
fun main() {
    val lines = readLines(22, 11, "data.txt")
    runMonkeyBusiness(lines, true, 20)
}

class Monkey(
    val items: ArrayList<Int>,
    val operation: (Int) -> Long,
    val divisibleBy: Int,
    val ifTrue: Int,
    val ifFalse: Int,
) {
    var activeness = 0L
}

fun String.parseOperation(): (Int) -> Long {
    when {
        startsWith("* old") -> return {
            val long = it.toLong()
            long * long
        }
        startsWith("* ") -> {
            val factor = substring(2).toLong()
            return { it * factor }
        }
        startsWith("+ ") -> {
            val added = substring(2).toLong()
            return { it + added }
        }
        startsWith("- ") -> {
            val subtracted = substring(2).toLong()
            return { it - subtracted }
        }
        else -> throw IllegalStateException("Unknown operation $this")
    }
}

fun lcm(a: Int, b: Int): Int {
    // could be improved, but not really necessary
    return Math.multiplyExact(a, b)
}

fun runMonkeyBusiness(lines: List<String>, divideBy3: Boolean, turns: Int) {
    val monkeys = ArrayList<Monkey>()
    var leastCommonMultiple = 1
    for (i in lines.indices step 7) {

        val id = monkeys.size
        checkEquals(lines[i], "Monkey $id:")

        val items =
            checkStartsWith(lines[i + 1], "  Starting items: ").split(", ").map { it.toInt() }
        val operation =
            checkStartsWith(lines[i + 2], "  Operation: new = old ").parseOperation()
        val divisibleBy =
            checkStartsWith(lines[i + 3], "  Test: divisible by ").toInt()
        val ifTrue =
            checkStartsWith(lines[i + 4], "    If true: throw to monkey ").toInt()
        val ifFalse =
            checkStartsWith(lines[i + 5], "    If false: throw to monkey ").toInt()

        check(id != ifTrue && id != ifFalse)

        monkeys.add(Monkey(ArrayList(items), operation, divisibleBy, ifTrue, ifFalse))
        leastCommonMultiple = lcm(leastCommonMultiple, divisibleBy)
    }

    println("LCM: $leastCommonMultiple")

    /*println("Before Round 1:")
    for (j in monkeys.indices) {
        println("Monkey $j: ${monkeys[j].items}")
    }*/

    for (i in 0 until turns) {
        for (j in monkeys.indices) {
            val monkey = monkeys[j]
            for (k in monkey.items.indices) {
                val oldItem = monkey.items[k]
                var itemL = monkey.operation(oldItem)
                if (divideBy3) itemL /= 3
                val newItem = (itemL % leastCommonMultiple).toInt()
                val inspect = newItem % monkey.divisibleBy == 0
                val target = monkeys[if (inspect) monkey.ifTrue else monkey.ifFalse]
                target.items.add(newItem)
            }
            monkey.activeness += monkey.items.size
            monkey.items.clear()
        }
        /*
        println("After Round ${i + 1}:")
        for (j in monkeys.indices) {
            println("Monkey $j: ${monkeys[j].items}")
        }*/
    }

    println("Activeness: ${monkeys.map { it.activeness }}")
    val (a0, a1) = monkeys.map { it.activeness }.sortedDescending()
    val monkeyBusiness = a0 * a1
    println("Monkey Business: $monkeyBusiness")
}

fun <V> checkEquals(a: V, b: V) {
    if (a == b) return
    throw IllegalStateException("Expected $a = $b")
}

fun checkStartsWith(a: String, b: String): String {
    if (a.startsWith(b)) return a.substring(b.length)
    throw IllegalStateException("Expected '$a'.startsWith('$b')")
}