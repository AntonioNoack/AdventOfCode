package me.anno.aoc24.day6

data class Vector2i(val x: Int, val y: Int) {
    operator fun plus(other: Vector2i): Vector2i {
        return Vector2i(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2i): Vector2i {
        return Vector2i(x - other.x, y - other.y)
    }

    operator fun times(s: Int): Vector2i {
        return Vector2i(x * s, y * s)
    }

    fun cross(other: Vector2i): Int {
        return x * other.y - y * other.x
    }

    fun crossL(other: Vector2i): Long {
        return x.toLong() * other.y.toLong() - y.toLong() * other.x.toLong()
    }

    override fun toString(): String {
        return "($x $y)"
    }
}