package me.anno.aoc24.day13

import java.math.BigInteger

data class Vector2ix(val x: BigInteger, val y: BigInteger) {

    operator fun plus(other: Vector2ix): Vector2ix {
        return Vector2ix(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2ix): Vector2ix {
        return Vector2ix(x - other.x, y - other.y)
    }

    operator fun times(s: BigInteger): Vector2ix {
        return Vector2ix(x * s, y * s)
    }

    operator fun div(s: BigInteger): Vector2ix {
        return Vector2ix(x / s, y / s)
    }

    fun cross(other: Vector2ix): BigInteger {
        return x * other.y - y * other.x
    }

    fun crossL(other: Vector2ix): Long {
        return x.toLong() * other.y.toLong() - y.toLong() * other.x.toLong()
    }

    fun dot(other: Vector2ix): BigInteger {
        return x * other.x + y * other.y
    }

    override fun toString(): String {
        return "($x $y)"
    }
}