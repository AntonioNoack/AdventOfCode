package me.anno.utils

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Vector2i(var x: Int, var y: Int) {

    operator fun plus(other: Vector2i): Vector2i {
        return Vector2i(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2i): Vector2i {
        return Vector2i(x - other.x, y - other.y)
    }

    operator fun times(s: Int): Vector2i {
        return Vector2i(x * s, y * s)
    }

    operator fun div(s: Int): Vector2i {
        return Vector2i(x / s, y / s)
    }

    fun posMod(o: Vector2i): Vector2i {
        return Vector2i(Maths.posMod(x, o.x), Maths.posMod(y, o.y))
    }

    fun cross(other: Vector2i): Int {
        return x * other.y - y * other.x
    }

    fun crossL(other: Vector2i): Long {
        return x.toLong() * other.y.toLong() - y.toLong() * other.x.toLong()
    }

    fun dot(other: Vector2i): Int {
        return x * other.x + y * other.y
    }

    fun manhattanDistance(other: Vector2i): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun max(o: Vector2i): Vector2i {
        return Vector2i(max(x, o.x), max(y, o.y))
    }

    fun min(o: Vector2i): Vector2i {
        return Vector2i(min(x, o.x), min(y, o.y))
    }

    override fun toString(): String {
        return "($x $y)"
    }
}