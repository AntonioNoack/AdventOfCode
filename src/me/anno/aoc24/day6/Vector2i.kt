package me.anno.aoc24.day6

data class Vector2i(val x: Int, val y: Int) {
    operator fun plus(other: Vector2i): Vector2i {
        return Vector2i(x + other.x, y + other.y)
    }
}