// https://adventofcode.com/2023/day/21
fun main() {
    val today = "Day21"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        initMap(input)
        return codeMap["root"]!!.calcValue()
    }

    fun part2(input: List<String>): Long {
        initMap(input)
        val (first, second) = (codeMap["root"]!! as ExpCode)
        val r1 = codeMap[first]!!.calcValue()
        val (nextKey, r) = if (reachedHumn) first to codeMap[second]!!.calcValue() else second to r1
        return determineHumn(nextKey, r)
    }

    chkTestInput(part1(testInput), 152L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 301L, Part2)
    println("[Part2]: ${part2(input)}")
}

val codeMap = mutableMapOf<String, Code>()
var reachedHumn = false //part2

fun initMap(input: List<String>) {
    codeMap.clear()
    input.forEach {
        it.split(": ").also { arr ->
            codeMap[arr[0]] = if (arr[1].matches("\\d+".toRegex())) NumberCode(arr[1].toLong()) else arr[1].split(" ").let { ExpCode(it[0], it[2], it[1]) }
        }
    }
}

fun determineHumn(key: String, expected: Long): Long {
    if (key == "humn") return expected
    reachedHumn = false
    val exp = (codeMap[key]!! as ExpCode)
    val rl = codeMap[exp.first]!!.calcValue().also { if (exp.first == "humn") reachedHumn = true }
    val (nextKey, r) = if (reachedHumn) exp.first to codeMap[exp.second]!!.calcValue() else exp.second to rl

    val nextResult = when (exp.op) {
        "+" -> expected - r
        "-" -> if (nextKey == exp.first) expected + r else r - expected
        "*" -> expected / r
        "/" -> if (nextKey == exp.first) expected * r else r / expected
        else -> throw IllegalStateException()
    }
    return determineHumn(nextKey, nextResult)
}

interface Code {
    fun calcValue(): Long
}

data class NumberCode(val value: Long) : Code {
    override fun calcValue(): Long = value
}

data class ExpCode(val first: String, val second: String, val op: String) : Code {

    override fun calcValue(): Long = when (op) {
        "+" -> codeMap[first]!!.calcValue() + codeMap[second]!!.calcValue()
        "-" -> codeMap[first]!!.calcValue() - codeMap[second]!!.calcValue()
        "*" -> codeMap[first]!!.calcValue() * codeMap[second]!!.calcValue()
        "/" -> codeMap[first]!!.calcValue() / codeMap[second]!!.calcValue()
        else -> throw IllegalStateException()
    }.also { if ("humn" in setOf(first, second)) reachedHumn = true }
}
