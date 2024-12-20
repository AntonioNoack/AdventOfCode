package me.anno.aoc23.day24


fun simplexAlgorithm(
    v0: DoubleArray,
    firstStepSize: Double,
    goodEnoughError: Double,
    maxSteps: Int,
    expansion: Double,
    contraction: Double,
    err: (v1: DoubleArray) -> Double
): Pair<Double, DoubleArray> {

    val l = v0.size

    val steps = DoubleArray(l) {
        firstStepSize
    }

    var lastError = err(v0)

    // 1e-16, but there may be numerical issues,
    // which cause stair-stepping, which would be an issue
    val precision = 1e-14

    var stepCtr = 0
    do {
        var wasChanged = false
        for (axis in 0 until l) {
            // quickly alternating the axes results in not
            // needing to follow straightly to the hole
            // (25: 303 steps, 3: 48 steps for Himmelblau's function, and 1e-6 error)
            for (i in 0 until 3) {
                val step = steps[axis]
                val lastX = v0[axis]
                val nextX = lastX + step
                v0[axis] = nextX
                val nextError = err(v0)
                if (nextError <= goodEnoughError) return Pair(nextError, v0)
                if (nextError < lastError) {
                    // better: expand and keep
                    steps[axis] = step * expansion
                    lastError = nextError
                    wasChanged = true
                } else {
                    // worse: contract and reset
                    val newStep = step * contraction
                    val minStepAllowed = kotlin.math.abs(lastX * precision)
                    val allowedNewStep = if (kotlin.math.abs(newStep) < minStepAllowed) {
                        if (step < 0) minStepAllowed else -minStepAllowed // alternate sign
                    } else {
                        newStep
                    }
                    // what, if the next step is just too small
                    // -> see steps as a change
                    steps[axis] = allowedNewStep
                    if (kotlin.math.abs(allowedNewStep) < kotlin.math.abs(newStep)) {
                        wasChanged = true
                    }
                    v0[axis] = lastX
                }
            }
        }
    } while (wasChanged && stepCtr++ < maxSteps)

    return Pair(lastError, v0)
}