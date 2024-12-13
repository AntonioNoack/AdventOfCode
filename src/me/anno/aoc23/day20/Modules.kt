package me.anno.aoc23.day20

abstract class Module {

    lateinit var name: String
    var nextTurn = ArrayList<Signal>()
    var thisTurn = ArrayList<Signal>()
    val destinations = ArrayList<Module>()

    abstract fun process()

    open fun send(signals: List<Signal>, src: Module) {
        printSend(signals)
        nextTurn.addAll(signals)
    }

    fun printSend(signals: List<Signal>) {
        if (signals.isNotEmpty()) {
            lowCtr += signals.count { it == Signal.LOW }
            highCtr += signals.count { it == Signal.HIGH }
            // println("${src.name} -${signals.joinToString()}> $name")
        }
    }

    fun swap() {
        val tmp = nextTurn
        nextTurn = thisTurn
        thisTurn = tmp
        nextTurn.clear()
    }

    open fun register(src: Module) {}
    open fun serialize(dst: BitStream) {}

    open fun reset() {
        thisTurn.clear()
        nextTurn.clear()
    }

    override fun toString() = name

}

class Broadcaster : Module() {
    override fun process() {
        for (dst in destinations) {
            dst.send(thisTurn, this)
        }
    }
}

// %
class FlipFlop : Module() {

    var isOn = false
    val tmp = ArrayList<Signal>()

    override fun process() {
        for (signal in thisTurn) {
            if (signal == Signal.HIGH) continue
            if (signal == Signal.LOW) {
                val toSend = if (isOn) Signal.LOW else Signal.HIGH
                // println("toggling $name, sending $toSend")
                tmp.add(toSend)
                isOn = !isOn
            }
        }
        for (dst in destinations) {
            dst.send(tmp, this)
        }
        tmp.clear()
    }

    override fun serialize(dst: BitStream) {
        dst.add(isOn)
    }

    override fun reset() {
        isOn = false
    }
}

// &
class Conjunction : Module() {

    private val memory = LinkedHashMap<Module, Signal>()

    override fun register(src: Module) {
        memory[src] = Signal.LOW
    }

    override fun send(signals: List<Signal>, src: Module) {
        printSend(signals)
        for (signal in signals) {
            memory[src] = signal
            val toSend = if (memory.values.all { it == Signal.HIGH }) Signal.LOW else Signal.HIGH
            // println("conj $name: $memory -> $toSend")
            nextTurn.add(toSend)
        }
    }

    override fun process() {
        for (dst in destinations) {
            dst.send(thisTurn, this)
        }
    }

    override fun serialize(dst: BitStream) {
        for ((_, signal) in memory) {
            dst.add(signal == Signal.HIGH)
        }
    }

    override fun reset() {
        memory.mapValues { Signal.LOW }
    }
}

class Output : Module() {
    override fun process() {}
}

class Rx : Module() {
    override fun process() {
        if (rxShouldCrash && thisTurn.any { it == Signal.LOW }) {
            throw RuntimeException("Found RX")
        }
    }
}
