package me.anno.utils

class Matrix2i(val a: Int, val b: Int, val c: Int, val d: Int) {

    fun inverted(): Pair<Matrix2i, Int> {
        return Matrix2i(d, -b, -c, a) to (a * d - b * c)
    }

    fun transform(v: Vector2i): Vector2i {
        return Vector2i(a * v.x + b * v.y, c * v.x + d * v.y)
    }
}