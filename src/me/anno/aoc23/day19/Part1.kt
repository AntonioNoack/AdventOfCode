package me.anno.aoc23.day19

import me.anno.utils.Utils.readLines

val order = "xmas"

class Rule(val case: Int, val lessThan: Boolean, val number: Int, val ifTrueName: String) {
    lateinit var ifTrue: RuleSet

    fun check(task: Task): Boolean {
        val xmas = task.xmas
        val x = xmas[case]
        return if (lessThan) {
            x < number
        } else {
            x > number
        }
    }
}

class RuleSet(val name: String, val rules: List<Rule>, val ifFalseName: String) {
    lateinit var ifFalse: RuleSet

    fun check(task: Task): RuleSet {
        for (rule in rules) {
            if (rule.check(task)) {
                return rule.ifTrue
            }
        }
        return ifFalse
    }
}

class Task(val xmas: IntArray)

fun parseRule(line: String): Rule {
    // a<2006:qkq
    val case = order.indexOf(line[0])
    val lessThan = line[1] == '<'
    val i0 = line.indexOf(':')
    val number = line.substring(2, i0).toInt()
    val ifTrueName = line.substring(i0 + 1)
    return Rule(case, lessThan, number, ifTrueName)
}

fun parseRuleSet(line: String): RuleSet {
    val i0 = line.indexOf('{')
    val name = line.substring(0, i0)
    val rules = line.substring(i0 + 1, line.length - 1).split(',')
    val ifTrue = rules.subList(0, rules.size - 1).map { parseRule(it) }
    return RuleSet(name, ifTrue, rules.last())
}

fun parseTask(line: String): Task {
    // {x=787,m=2655,a=1222,s=2876}
    val cleaned = line.filter { it in '0'..'9' || it == ',' }
        .split(',').map { it.toInt() }
    return Task(cleaned.toIntArray())
}

fun linkRules(ruleSets: List<RuleSet>): RuleSet {
    val byName = ruleSets.associateBy { it.name }
    for (ruleSet in ruleSets) {
        ruleSet.ifFalse = byName[ruleSet.ifFalseName]!!
        for (rule in ruleSet.rules) {
            rule.ifTrue = byName[rule.ifTrueName]!!
        }
    }
    return byName["in"]!!
}

fun simple(name: String): RuleSet {
    return RuleSet(name, emptyList(), name)
}

val reject = simple("R")
val accept = simple("A")

fun main() {
    val lines = readLines(23, 19, "data.txt")
    val empty = lines.indexOf("")
    val rules = lines.subList(0, empty).map { parseRuleSet(it) } + listOf(reject, accept)
    val inRule = linkRules(rules)
    val tasks = lines.subList(empty + 1, lines.size).map { parseTask(it) }
    val okTasks = tasks.filter { runWorkflow(it, inRule) }
    val accepted = okTasks.sumOf { it.xmas.sum() }
    println(accepted)
}

fun runWorkflow(task: Task, inRule: RuleSet): Boolean {
    var rule = inRule
    while (true) {
        rule = rule.check(task)
        if (rule === reject) return false
        if (rule === accept) return true
    }
}