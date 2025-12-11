package me.anno.aoc25.day11

import me.anno.utils.Utils.readLines

/**
 * 
 *  find all paths, which include dac and fft...
 *   sequences: svr -> dac -> fft -> out
 *   sequences: svr -> fft -> dac -> out
 *
 *  the graph is a tree? boring XD, I had prepared for the eventuality, it is not,
 *   but idk if that had worked, let's test it :)
 *  -> it works :D
 * 
 * */
fun main() {
    
    val graph = Graph(readLines(25, 11, "evilSample.txt"))
    val n0 = "svr"
    val n1 = "dac"
    val n2 = "fft"
    val n3 = "out"

    val p01 = graph.countPaths(n0, n1)
    val p02 = graph.countPaths(n0, n2)
    val p12 = graph.countPaths(n1, n2)
    val p21 = graph.countPaths(n2, n1)
    val p23 = graph.countPaths(n2, n3)
    val p13 = graph.countPaths(n1, n3)

    val totalDacFft = p01 * p12 * p23
    val totalFftDac = p02 * p21 * p13
    val total = totalDacFft + totalFftDac
    println("Total: $totalDacFft + $totalFftDac = $total")
}