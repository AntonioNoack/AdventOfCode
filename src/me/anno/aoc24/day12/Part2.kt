package me.anno.aoc24.day12

import me.anno.utils.Utils.directions
import me.anno.utils.Vector2i
import me.anno.utils.Utils.readLines

fun main() {
    val field = readLines(24, 12, "data.txt")
    val plots = findPlots2(field)
    println(plots)
    println(plots.sumOf { price(it) })
}

data class Plot2(val symbol: Char, var area: Int, var sides: Int) {
    override fun toString(): String {
        return "['$symbol', $area², ${sides}]"
    }
}

fun price(plot: Plot2): Int {
    return plot.area * plot.sides
}

fun findPlots2(field: List<String>): List<Plot2> {
    val sy = field.size
    val sx = field[0].length
    val plots = HashMap<Vector2i, Plot2>()
    val result = ArrayList<Plot2>()
    for (y in 0 until sy) {
        for (x in 0 until sx) {
            val pos = Vector2i(x, y)
            if (pos in plots) continue
            val symbol = field[y][x]
            val plot = Plot2(symbol, 0, 0)
            val remaining = ArrayList<Vector2i>()
            remaining.add(pos)
            plots[pos] = plot
            while (true) {
                val posI = remaining.removeLastOrNull() ?: break
                plot.area++
                for (di in directions.indices) {
                    val dir = directions[di]
                    val posJ = posI + dir
                    if (posJ.x in 0 until sx && posJ.y in 0 until sy) {
                        if (field[posJ.y][posJ.x] == symbol) {
                            if (posJ !in plots) {
                                plots[posJ] = plot
                                remaining.add(posJ)
                            } // else done
                        }
                    }
                }
            }
            result.add(plot)
        }
    }
    val nothing = ' '
    for (dir in directions) {
        if (dir.x == 0) {
            for (y in 0 until sy) {
                val onEdge = y + dir.y !in 0 until sy
                var lastSymbol = nothing
                for (x in 0 until sx) {
                    val symbol = field[y][x]
                    if (!onEdge && field[y + dir.y][x] == symbol) {
                        lastSymbol = nothing
                    } else {
                        if (symbol != lastSymbol) {
                            // else a new side begins :)
                            plots[Vector2i(x, y)]!!.sides++
                            lastSymbol = symbol
                        }
                    }
                }
            }
        } else {
            for (x in 0 until sx) {
                val onEdge = x + dir.x !in 0 until sx
                var lastSymbol = nothing
                for (y in 0 until sy) {
                    val symbol = field[y][x]
                    if (!onEdge && field[y][x + dir.x] == symbol) {
                        lastSymbol = nothing
                    } else {
                        if (symbol != lastSymbol) {
                            // else a new side begins :)
                            plots[Vector2i(x, y)]!!.sides++
                            lastSymbol = symbol
                        }
                    }
                }
            }
        }
    }
    return result
}