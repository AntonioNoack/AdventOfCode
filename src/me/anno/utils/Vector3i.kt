package me.anno.utils

import kotlin.math.max
import kotlin.math.min

data class Vector3i(var x: Int, var y: Int, var z: Int) {
    fun min(o: Vector3i): Vector3i {
        return Vector3i(min(x, o.x), min(y, o.y), min(z, o.z))
    }

    fun max(o: Vector3i): Vector3i {
        return Vector3i(max(x, o.x), max(y, o.y), max(z, o.z))
    }

    operator fun plus(o: Vector3i): Vector3i {
        return Vector3i(x + o.x, y + o.y, z + o.z)
    }

    override fun toString(): String {
        return "($x $y $z)"
    }
}
