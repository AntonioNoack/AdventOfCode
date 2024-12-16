package me.anno.aoc24.day12

import me.anno.utils.Utils.directions
import me.anno.utils.Vector2i
import me.anno.utils.Utils.readLines

fun main() {
    val field = readLines(24, 12, "data.txt")
    val plots = findPlots(field)
    println(plots)
    println(plots.sumOf { price(it) })
}

data class Plot(val symbol: Char, var area: Int, var perimeter: Int) {
    override fun toString(): String {
        return "['$symbol', $area², $perimeter]"
    }
}

fun price(plot: Plot): Int {
    return plot.area * plot.perimeter
}

fun findPlots(field: List<String>): List<Plot> {
    val sy = field.size
    val sx = field[0].length
    val existingPlots = HashMap<Vector2i, Plot>()
    val result = ArrayList<Plot>()
    for (y in 0 until sy) {
        for (x in 0 until sx) {
            val pos = Vector2i(x, y)
            if (pos in existingPlots) continue
            val symbol = field[y][x]
            val plot = Plot(symbol, 0, 0)
            val remaining = ArrayList<Vector2i>()
            remaining.add(pos)
            existingPlots[pos] = plot
            while (true) {
                val posI = remaining.removeLastOrNull() ?: break
                plot.area++
                for (dir in directions) {
                    val posJ = posI + dir
                    if (posJ.x in 0 until sx && posJ.y in 0 until sy) {
                        if (field[posJ.y][posJ.x] == symbol) {
                            if (posJ !in existingPlots) {
                                existingPlots[posJ] = plot
                                remaining.add(posJ)
                            } // else done
                        } else plot.perimeter++
                    } else plot.perimeter++
                }
            }
            result.add(plot)
        }
    }
    return result
}