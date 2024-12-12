package me.anno.aoc23.day15

data class LabelledLens(val label: String, var lensType: Char) {
    override fun toString(): String {
        return "[$label $lensType]"
    }

    fun calcFocussingPower(boxNumber: Int, slowNumber: Int): Int {
        return (boxNumber + 1) * slowNumber * (lensType - '0')
    }
}

fun main() {

    val boxes = Array(256) { ArrayList<LabelledLens>() }
    val data = readParts("data.txt")
    for (instruction in data) {
        if (instruction.endsWith('-')) {
            val label = instruction.substring(0, instruction.length - 1)
            val box = boxes[hash(label)]
            box.removeIf { it.label == label }
        } else {
            assert(instruction[instruction.length - 2] == '=')
            assert(instruction.last() in '1'..'9')
            val label = instruction.substring(0, instruction.length - 2)
            val lensType = instruction.last()
            val box = boxes[hash(label)]
            val slotWithSameLabel = box.firstOrNull { it.label == label }
            if (slotWithSameLabel != null) {
                slotWithSameLabel.lensType = lensType
            } else {
                box.add(LabelledLens(label, lensType))
            }
        }
    }

    var totalPower = 0
    for (bi in boxes.indices) {
        val box = boxes[bi]
        if (box.isEmpty()) continue
        println("Box $bi: ${box.joinToString(" ")}")
        totalPower += box.withIndex().sumOf { (idx, lens) ->
            lens.calcFocussingPower(bi, idx + 1)
        }
    }
    println("Total power: $totalPower")

}