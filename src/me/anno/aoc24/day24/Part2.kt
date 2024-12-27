package me.anno.aoc24.day24

import me.anno.utils.Maths.hasFlag
import me.anno.utils.Utils.desktop
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

// todo find swapped output wires...

data class Vector2f(val x: Float, val y: Float, val color: Color)

fun setAlias(key: String, value: String) {
    if (key.startsWith("x") || key.startsWith("y") || key.startsWith("z")) {
        throw IllegalStateException("Alias for $key")
    }
    val prev = aliases.put(key, value)
    if (prev != null) println("Override alias $key -> $prev | $value")
}

val bits = 45
val points = HashMap<String, Vector2f>()

val swappedList = ArrayList<String>()
fun swap(a: String, b: String, formByResult: MutableMap<String, Formula>) {
    val fa = formByResult[a]!!
    val fb = formByResult[b]!!
    fa.result = b
    fb.result = a
    formByResult[b] = fa
    formByResult[a] = fb
    swappedList.add(a)
    swappedList.add(b)
}

fun swap2(ra: String, rb: String, formByResult: MutableMap<String, Formula>): () -> Unit {
    val fa = formByResult[ra]!!
    val fb = formByResult[rb]!!
    formByResult[rb] = Formula(fa.a, fa.type, fa.b, rb)
    formByResult[ra] = Formula(fb.a, fb.type, fb.b, ra)
    return {
        // undo function
        formByResult[ra] = fa
        formByResult[rb] = fb
    }
}

val xNames = (0 until 64).map { "x${it.toString().padStart(2, '0')}" }
val yNames = (0 until 64).map { "y${it.toString().padStart(2, '0')}" }
val zNames = (0 until 64).map { "z${it.toString().padStart(2, '0')}" }

fun isAdditionCorrect(
    bits0: Int, bits1: Int,
    formulaByResult: Map<String, Formula>
): Boolean {

    val values = HashMap<String, Int>()

    fun getValue0(name: String, depth: Int): Int {
        return values.getOrPut(name) {
            if (name.startsWith("x") || name.startsWith("y")) {
                // simple
                0
            } else {
                val formula = formulaByResult[name]
                if (formula != null && depth < 4 * 64) run {
                    val va = getValue0(formula.a, depth + 1)
                    val vb = getValue0(formula.b, depth + 1)
                    if (va in 0..1 && vb in 0..1) when (formula.type) {
                        andType -> va and vb
                        orType -> va or vb
                        xorType -> va xor vb
                        else -> throw IllegalStateException()
                    } else -1
                } else -1
            }
        }
    }

    fun getValue(name: String): Boolean? {
        return when (getValue0(name, 0)) {
            0 -> false
            1 -> true
            else -> null
        }
    }

    val numBits = bits1 - bits0 + 1
    for (vx in 0 until (1 shl numBits)) {
        for (vy in 0 until (1 shl numBits)) {
            values.clear() // delete all intermediate results
            val vz = vx + vy
            // fill in required values
            for (i in 0 until numBits) {
                values[xNames[i + bits0]] = if (vx.hasFlag(1 shl i)) 1 else 0
                values[yNames[i + bits0]] = if (vy.hasFlag(1 shl i)) 1 else 0
            }
            // check result
            for (i in 0..numBits) {
                if (getValue(zNames[i + bits0]) != vz.hasFlag(1 shl i)) {
                    return false
                }
            }
        }
    }
    return true
}

fun findAllConnected(
    questionable: Collection<String>,
    byResult: MutableMap<String, Formula>
): Set<String> {
    val toCheck = HashSet<String>()
    toCheck.addAll(questionable)
    for ((_, form) in byResult) {
        if (form.a in questionable || form.b in questionable ||
            form.result in questionable
        ) {
            toCheck.add(form.a)
            toCheck.add(form.b)
            toCheck.add(form.result)
        }
    }
    return toCheck
}

fun checkAddition(
    bits0: Int, bits1: Int, c0: Collection<String>,
    byResult: MutableMap<String, Formula>
) {
    // find all nodes connected to <c0>
    val c1 = findAllConnected(c0, byResult)
    val c2 = findAllConnected(c1, byResult)
    // for all pairs of them, check whether addition is correct
    val toCheckList = c2.filter { it in byResult }.toList()
    println("toCheck: $toCheckList")
    if (isAdditionCorrect(bits0, bits1, byResult)) {
        println("Default Valid [$bits0, $bits1]")
        return
    }
    var ctr = 0
    var ctr2 = 0
    for (i in toCheckList.indices) {
        for (j in i + 1 until toCheckList.size) {
            val undo = swap2(toCheckList[i], toCheckList[j], byResult)
            if (isAdditionCorrect(bits0, bits1, byResult)) {
                println("Swap Valid: ${toCheckList[i]}, ${toCheckList[j]}")
                ctr++
            } else ctr2++
            undo()
        }
    }
    println("$ctr/$ctr2")
}

/**
 * Read out the data,
 * color it by what we would expect to visually find, which sections need fixing:
 * luckily, all four sections were separated.
 * then I coded a procedure to find the to-be-swapped nodes by checking the addition;
 * then I applied these changes - what's printed to an image now is the fixed adder
 * */
fun main() {

    val (values, formulas0) = readNetwork("data.txt")
    val formByResult = formulas0.associateBy { it.result }.toMutableMap()

    checkAddition(6, 9, listOf("dnn", "ffj"), formByResult)
    swap("z08", "ffj", formByResult) // unique solution

    checkAddition(13, 16, listOf("tpr", "knt", "pwn"), formByResult)
    swap("kfm", "dwp", formByResult) // unique solution

    checkAddition(20, 23, listOf("jbd", "pgt", "tjh"), formByResult)
    // no solution is found :(

    checkAddition(20, 23, formByResult.keys, formByResult)
    swap("z22", "gjh", formByResult) // finally found unique solution :)

    checkAddition(29, 32, listOf("vhw", "hnn"), formByResult)
    swap("z31", "jdr", formByResult)// unique solution

    println("sorted swaps: ${swappedList.sorted().joinToString(",")}")

    val formulas = formByResult.values.toMutableList()
    val intermediateSymbols = formulas0
        .map { it.result }
        .filter { !it.startsWith("z") }

    for ((i, symbol) in intermediateSymbols.withIndex()) {
        points[symbol] = Vector2f(i * bits / (intermediateSymbols.size + 1f), -1f, Color.GRAY)
    }

    val numBits = values.keys.count { it.startsWith("y") }
    val numBitsX = values.keys.count { it.startsWith("x") }
    val numBitsZ = formulas.count { it.result.startsWith("z") }
    if (numBits != numBitsX || numBits != numBitsZ - 1)
        throw IllegalStateException("$numBits + $numBitsX = $numBitsZ")

    for (i in 0 until numBits) {
        points["x${i.toString().padStart(2, '0')}"] =
            Vector2f(i.toFloat() - 0.25f, 0f, Color(0x33ff33))
        points["y${i.toString().padStart(2, '0')}"] =
            Vector2f(i.toFloat() + 0.25f, 0f, Color(0x4BDEFF))
    }
    for (i in 0..numBits) {
        points["z${i.toString().padStart(2, '0')}"] =
            Vector2f(i.toFloat(), 3f, Color.WHITE)
    }

    val x_xor_y = formulas.filterOut {
        it.type == xorType &&
                it.min.startsWith("x") &&
                it.max.startsWith("y") &&
                it.a.substring(1) == it.b.substring(1) &&
                (it.result == "z00" || !it.result.startsWith("z"))
    }

    val x_and_y = formulas.filterOut {
        it.type == andType &&
                it.min.startsWith("x") &&
                it.max.startsWith("y") &&
                it.a.substring(1) == it.b.substring(1) &&
                !it.result.startsWith("z")
    }

    val carryInputs = x_xor_y.associateBy { it.result }
    val carry = formulas.filterOut {
        it.type == xorType &&
                (it.a in carryInputs || it.b in carryInputs) &&
                (it.a !in carryInputs || it.b !in carryInputs) &&
                it.result.startsWith("z") &&
                (carryInputs[it.a] ?: carryInputs[it.b])!!.a.substring(1) == it.result.substring(1)
    }

    for (form in x_xor_y) {
        if (form.result == "z00") continue
        val i = form.a.substring(1).toInt()
        setAlias(form.result, "xor${form.a.substring(1)}")
        points[form.result] = Vector2f(i - 0.25f, 1.2f, Color.GREEN)
    }

    for (form in x_and_y) {
        val i = form.a.substring(1).toInt()
        setAlias(form.result, "xnd${form.a.substring(1)}")
        points[form.result] = Vector2f(i + 0.25f, 1.2f, Color.CYAN)
    }

    for (form in carry) {
        val src = if (form.a in carryInputs) form.b else form.a
        val i = form.result.substring(1).toInt()
        setAlias(src, "cin${form.result.substring(1)}")
        points[src] = Vector2f(i - 0.9f, 2.2f, Color.MAGENTA)
    }

    val carry_and = formulas.filterOut {
        it.type == andType &&
                it.minName.startsWith("cin") &&
                it.maxName.startsWith("xor") &&
                !it.result.startsWith("z") &&
                it.minName.substring(3) == it.maxName.substring(3)
    }

    for (form in carry_and) {
        val i = form.minName.substring(3).toInt()
        setAlias(form.result, "can${form.minName.substring(3)}")
        points[form.result] = Vector2f(i - 0.4f, 2.2f, Color.RED)
    }

    val carry_res = formulas.filterOut {
        it.type == orType &&
                it.minName.startsWith("can") &&
                it.maxName.startsWith("xnd") &&
                it.resultName.startsWith("cin") &&
                it.minName.substring(3) == it.maxName.substring(3) &&
                it.minName.substring(3).toInt() + 1 == it.resultName.substring(3).toInt()
    }

    println("#bits: $numBits")
    println("#xor: ${x_xor_y.size}")
    println("#and: ${x_and_y.size}")
    println("#carry: ${carry.size}")
    println("#carry-and: ${carry_and.size}")
    println("#carry-res: ${carry_res.size}")
    println(formulas
        .map { it.toString() }
        .sorted()
        .joinToString("\n")
    )

    println("${formulas.size}/${formulas0.size}")

    // find positions for entries without alias
    fun findAlias(name: String) {
        if (name in aliases) return
        if (name.any { it in '0'..'9' }) return
        var sumX = 0f
        var sumW = 0f
        fun add(name1: String?) {
            if (name1 == null || name1 == name) return
            val pt = points[name1] ?: return
            sumX += pt.x
            sumW++
        }
        add(formByResult[name]?.a)
        add(formByResult[name]?.b)
        for (form in formulas0) {
            if (form.a == name || form.b == name) {
                add(form.result)
            }
        }
        if (sumW > 0f) {
            points[name] = Vector2f(sumX / sumW, -1f, Color.ORANGE)
        }
    }

    for (form in formulas) {
        findAlias(form.a)
        findAlias(form.b)
        findAlias(form.result)
    }

    // find all broken wires...
    //  8 out of 222 = 222 * 221 * 222 * ... ~ 222^8 ~ 62 bits, *8!

    // x00 xor y00 -> z00
    printGraph(formulas0)

}

fun printGraph(formulas: List<Formula>) {

    // todo if x, y or z, or has alias, give special color,
    //  else assign "missing" color
    // todo draw all points with their names
    // todo draw all connections

    val width = 4096
    val height = 400
    val image = BufferedImage(width, height, 1)
    val gfx = image.graphics
    val g2 = gfx as? Graphics2D
    g2?.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

    fun x(x: Float) = (width * (x + 1f) / (bits + 2f)).toInt()
    fun y(y: Float) = height - (height * (y + 1.5f) / 5f).toInt()

    val fontSize = gfx.font.size
    for ((pt, pos) in points) {
        gfx.color = pos.color
        gfx.drawRect(x(pos.x), y(pos.y), 1, 1)
        val alias = aliases[pt]
        gfx.drawString(alias ?: pt, x(pos.x) + 5, y(pos.y))
        if (alias != null) {
            gfx.drawString(pt, x(pos.x) + 5, y(pos.y) + fontSize)
        }
    }

    val dy = 0.07f
    for (form in formulas) {
        val a = points[form.a]!!
        val b = points[form.b]!!
        val c = points[form.result]!!
        val cx = x(c.x)
        val cy = y(c.y - dy)
        gfx.color = a.color
        gfx.drawLine(x(a.x), y(a.y + dy), cx, cy)
        gfx.color = b.color
        gfx.drawLine(x(b.x), y(b.y + dy), cx, cy)
        gfx.color = c.color
        gfx.drawLine(cx, cy, cx, y(c.y + dy))
    }

    gfx.dispose()
    ImageIO.write(image, "png", File(desktop, "adder.png"))
}

fun <V> MutableList<V>.filterOut(predicate: (V) -> Boolean): List<V> {
    val removed = ArrayList<V>()
    removeIf {
        val removeIt = predicate(it)
        if (removeIt) removed.add(it)
        removeIt
    }
    return removed
}