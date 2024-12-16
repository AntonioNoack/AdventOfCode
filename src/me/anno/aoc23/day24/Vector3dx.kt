package me.anno.aoc23.day24

import java.math.BigDecimal
import kotlin.math.sqrt

data class Vector3dx(var x: BigDecimal, var y: BigDecimal, var z: BigDecimal) {
    fun lengthSquared() = x * x + y * y + z * z
    fun dot(other: Vector3dx): BigDecimal {
        return x * other.x + y * other.y + z * other.z
    }

    operator fun plus(other: Vector3dx): Vector3dx {
        return Vector3dx(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vector3dx): Vector3dx {
        return Vector3dx(x - other.x, y - other.y, z - other.z)
    }

    operator fun times(s: BigDecimal): Vector3dx {
        return Vector3dx(x * s, y * s, z * s)
    }

    fun normalized(length: Double): Vector3dx {
        return times(BigDecimal(length / sqrt(lengthSquared().toDouble())))
    }
}