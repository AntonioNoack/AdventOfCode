package me.anno.aoc23.day24

import me.anno.aoc23.day22.ObjWriter
import me.anno.utils.Utils.desktop
import me.anno.utils.Utils.readLines
import java.io.File
import java.math.BigDecimal

fun main() {
    // to do find the position xyz and velocity abc, such that there is a t>0 for each hail stone, where it
    //  and the new ray intersect
    val lines = readLines(23, 24, "data.txt")
    val hails = lines.subList(1, lines.size)
        .map { parseHail(it) }
    val line0 = lines[0].split(',').map { it.toDouble() }
    val name = "hail"
    val writer = ObjWriter("$name.mtl")
    val obj = writer.obj.append(
        "o lines\n" +
                "usemtl lines\n"
    )
    val mtl = writer.mtl.append(
        "newmtl lines\n" +
                "Kd 1.0 1.0 1.0\n\n"
    )
    val invScale = (line0[1] - line0[0])
    val invScaleI = BigDecimal(invScale * 0.001)
    val scale = 1.0 / invScale
    for (hail in hails) {
        fun append(pos: Vector3dx) {
            obj.append("v ").append(pos.x.toDouble() * scale)
                .append(' ').append(pos.y.toDouble() * scale)
                .append(' ').append(pos.z.toDouble() * scale)
                .append('\n')
        }
        append(hail.position)
        append(hail.position + hail.velocity * invScaleI)
        obj.append("l ").append(writer.vi++)
            .append(' ').append(writer.vi++)
            .append('\n')
    }
    File(desktop, "$name.obj").writeText(obj.toString())
    File(desktop, "$name.mtl").writeText(mtl.toString())
}