package me.anno.aoc22.day1

import me.anno.utils.Utils.readLines
import me.anno.utils.Utils.split

fun readRations(): List<List<Int>> {
    return  readLines(22,1,"data.txt")
        .split("")
        .map { line -> line.map { it.toInt() } }
}

fun main(){
    val rations = readRations()
    println(rations)
    println("max calories: ${rations.maxOf { it.sum() }}")
}