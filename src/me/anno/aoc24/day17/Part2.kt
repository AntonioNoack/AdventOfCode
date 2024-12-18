package me.anno.aoc24.day17

import me.anno.utils.Utils.readLines
import sun.reflect.generics.reflectiveObjects.NotImplementedException
import java.math.BigInteger
import kotlin.math.max

lateinit var raw: List<Int>

fun defineMachine(): Machine {
    val machine = Machine()
    val lines = readLines(24, 17, "data.txt")
    defineRegisters(lines, machine)
    raw = parseInstructionsRaw(lines[4])
    parseInstructions(raw, machine)
    return machine
}

fun main() {
    val machine = defineMachine()
    val regA0 = machine.registers[0].toLong()
    val numberOfBits = raw.size * 3
    // 48 bits -> we can use longs instead of biginteger :)
    println("target: $raw, num bits: $numberOfBits")
    machine.registers[0] = BigInteger.valueOf(136902148098458L)
    machine.run()
    println(machine.output)
    println(optimizedMachine(regA0))
    raw += endDigit
    recursiveTest(0L, 0)
    println("Solution: $minSolution")
    // a valid solution:            136902148098458
    // apparently still too high:   109156923705754
    // solution with 8-bit-padding: 109019476330651, is correct :)
}

var bestLen = 0

var minSolution = Long.MAX_VALUE

fun recursiveTest(bits: Long, i: Int) {
    val expectedDigits = expectedDigits(i)
    if (expectedDigits == raw.size) return
    if (fulfillsTask(bits, expectedDigits)) {
        if (i > bestLen) {
            bestLen = i
            val check = optimizedMachine(bits)
            println("best: $bits, $i, check: $check")
        }
        // how is the result ever lower twice??? am I not going from small to big numbers???
        // no, 1000 is checked before 0001, exactly incorrect
        if (expectedDigits >= raw.lastIndex && bits < minSolution) {
            minSolution = bits
            println("Found better solution: $bits")
        }
        val extraMask = 1L shl i
        recursiveTest(bits, i + 1)
        recursiveTest(bits + extraMask, i + 1)
    }
}

fun expectedDigits(i: Int): Int {
    return max(i - 8, 0) / 3
}

fun fulfillsTask(bits: Long, expectedDigits: Int): Boolean {
    var a = bits
    for (j in 0 until expectedDigits) {
        if (nextDigit(a) != raw[j]) return false
        a = a shr 3
    }
    return true
}

fun runOldMachine() {
    val machine = defineMachine()
    val regA0 = machine.registers[0]
    println("target: $raw")
    for ((i, instr) in machine.instructions.withIndex()) {
        printNicely(i, instr)
    }
    machine.run()
    println(machine.output)
    println(optimizedMachine(regA0.toLong()))
}

fun printNicely(i: Int, instruction: Instruction) {
    val (opcode, literal) = instruction
    fun combo(): String {
        return when (literal) {
            in 0..3 -> "$literal"
            in 4..6 -> "${('a' + (literal - 4))}"
            else -> throw NotImplementedException()
        }
    }

    val str = when (opcode) {
        Opcode.ADV -> "a = a >> ${combo()}"
        Opcode.BDV -> "b = a >> ${combo()}"
        Opcode.CDV -> "c = a >> ${combo()}"
        Opcode.BXL -> "b = b xor $literal"
        Opcode.BST -> "b = ${combo()} & 7"
        Opcode.JNZ -> "if(a != 0) goto $literal"
        Opcode.BXC -> "b = b xor c"
        Opcode.OUT -> "output(${combo()} & 7)"
    }
    println("[$i] $str")
}

fun optimizedMachine(a0: Long): List<Int> {
    var a = a0
    val result = ArrayList<Int>()
    while (a > 0L) {
        result.add(nextDigit(a))
        a = a.shr(3)
    }
    return result
}

val endDigit = -1

// 3 + 5 bits are relevant at max
// val relevantBitMask = BigInteger.valueOf(0xff)

fun nextDigit(a: Long): Int {
    if (a == 0L) return endDigit
    val masked = a.toInt()
    val b = masked.and(7) xor 5
    val c = masked shr b
    return (c xor b xor 6).and(7)
}