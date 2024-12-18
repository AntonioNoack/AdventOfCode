package me.anno.aoc24.day17

import me.anno.utils.Utils.readLines
import java.math.BigInteger

enum class Opcode {
    ADV,
    BXL,
    BST,
    JNZ,

    BXC,
    OUT,
    BDV,
    CDV
}

data class Instruction(val opcode: Opcode, val operand: Int)

val seven = BigInteger.valueOf(7)
val maxSupportedShift = BigInteger.valueOf(Int.MAX_VALUE.toLong())

fun parseInstructionsRaw(line: String): List<Int> {
    val si = line.indexOf(": ") + 2
    val numbers = line.substring(si).split(',')
        .map { it.toInt() }
    return numbers
}

fun parseInstructions(numbers: List<Int>, machine: Machine) {
    for (i in numbers.indices step 2) {
        machine.instructions.add(Instruction(Opcode.entries[numbers[i]], numbers[i + 1]))
    }
}

fun defineRegisters(lines: List<String>, machine: Machine) {
    for (i in 0 until 3) {
        val line = lines[i]
        val value = line.substring(line.indexOf(": ") + 2)
        machine.registers[i] = BigInteger(value)
    }
}

class Machine {

    val registers = Array<BigInteger>(3) { BigInteger.ZERO }
    val instructions = ArrayList<Instruction>()

    val output = ArrayList<Int>()
    var instructionPointer = 0

    fun run() {
        while (instructionPointer < instructions.size) {
            step()
        }
    }

    fun step() {

        val (opcode, literalOperand) = instructions[instructionPointer]
        fun getComboOperand(): BigInteger {
            return when (literalOperand) {
                in 0..3 -> BigInteger.valueOf(literalOperand.toLong())
                in 4..6 -> registers[literalOperand - 4]
                else -> throw IllegalStateException()
            }
        }

        fun adv(): BigInteger {
            val a = registers[0]
            val b = getComboOperand()
            return if (b <= maxSupportedShift) {
                a.shr(b.toInt())
            } else BigInteger.ZERO
        }

        when (opcode) {
            Opcode.ADV -> registers[0] = adv()
            Opcode.BDV -> registers[1] = adv()
            Opcode.CDV -> registers[2] = adv()
            Opcode.BST -> registers[1] = getComboOperand().and(seven)
            Opcode.BXC -> registers[1] = registers[1] xor registers[2]
            Opcode.BXL -> registers[1] = registers[1] xor BigInteger.valueOf(literalOperand.toLong())
            Opcode.OUT -> output.add((getComboOperand() and seven).toInt())
            Opcode.JNZ -> {
                if (registers[0] != BigInteger.ZERO) {
                    instructionPointer = literalOperand
                    return
                }
            }
        }

        instructionPointer++

    }
}

fun main() {
    val machine = Machine()
    val lines = readLines(24, 17, "data.txt")
    defineRegisters(lines, machine)
    val raw = parseInstructionsRaw(lines[4])
    parseInstructions(raw, machine)
    machine.run()
    println(machine.output.joinToString(","))
}