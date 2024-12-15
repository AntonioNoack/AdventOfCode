package me.anno.aoc23.day22

import me.anno.utils.Vector3i

class ObjWriter(mtlName: String) {

    var vi = 1
    var mi = 0
    var oi = 0

    val colorToMaterial = HashMap<Int, Int>()

    val obj = StringBuilder()
    val mtl = StringBuilder()

    init {
        obj.append("# AdventOfCode\n")
            .append("usemtl ").append(mtlName).append("\n\n")
    }

    val one = Vector3i(1, 1, 1)

    val faces = listOf(
        0, 1, 3, 2,
        1, 5, 7, 3,
        2, 3, 7, 6,
        0, 4, 5, 1,
        0, 2, 6, 4,
        4, 6, 7, 5
    )

    fun appendVertex(x: Int, y: Int, z: Int) {
        obj.append("v ").append(x).append(" ").append(z).append(" ").append(y).append("\n")
        vi++
    }

    fun appendFace(vi: Int, fi40: Int) {
        var fi4 = fi40
        obj.append("f")
        for (df in 0 until 4) {
            obj.append(" ").append(vi + faces[fi4++])
        }
        obj.append("\n")
    }

    fun getColor(color: Int, shift: Int): Float {
        return color.shr(shift).and(255) / 255f
    }

    fun append(brick: Brick, color: Int) {
        val mi = colorToMaterial.getOrPut(color) {
            // newmtl shinyred
            //        Ka  0.1986  0.0000  0.0000
            //        Kd  0.5922  0.0166  0.0000
            //        Ks  0.5974  0.2084  0.2084
            //        illum 2
            //        Ns 100.2237
            mtl.append("newmtl mtl").append(mi).append("\n")
                .append("Kd ").append(getColor(color, 16))
                .append(" ").append(getColor(color, 8))
                .append(" ").append(getColor(color, 0))
                .append("\n\n")
            mi++
        }
        obj.append("o obj").append(oi++).append("\n")
            .append("usemtl mtl").append(mi).append("\n")
        val vi = vi
        var (min, max) = brick
        max += one
        appendVertex(min.x, min.y, min.z)
        appendVertex(min.x, min.y, max.z)
        appendVertex(min.x, max.y, min.z)
        appendVertex(min.x, max.y, max.z)
        appendVertex(max.x, min.y, min.z)
        appendVertex(max.x, min.y, max.z)
        appendVertex(max.x, max.y, min.z)
        appendVertex(max.x, max.y, max.z)
        for (fi4 in faces.indices step 4) {
            appendFace(vi, fi4)
        }
    }
}