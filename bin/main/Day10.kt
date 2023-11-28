// https://adventofcode.com/2023/day/10
fun main() {
    val today = "Day10"

    val input = readInput(today)
    val testInput = readTestInput(today)

    val requiredCycles = listOf(20, 60, 100, 140, 180, 220)

    fun part1(input: List<String>): Int {
        val result = getCycleResultMapping(input)
        return requiredCycles.sumOf { it * result[it - 1] }
    }

    fun part2(input: List<String>): Int {
        val result = getCycleResultMapping(input)
        (0..239).map { c -> if (c % 40 in result[c].let { x -> x - 1..x + 1 }) "#" else "." }
            .chunked(40).forEach { println(it.joinToString("")) }
        return 0
    }

    chkTestInput(part1(testInput), 13140, Part1)
    println("[Part1]: ${part1(input)}")

    println("[Part2 - Test]:")
    part2(testInput)
    println("\n[Part2]:")
    part2(input) //PLPAFBCL
}

fun getCycleResultMapping(input: List<String>): MutableList<Int> {
    val result = mutableListOf(1) // [0] = 1
    input.forEach {
        when {
            it == "noop" -> result += result.last()
            it.startsWith("addx") -> result += listOf(result.last(), result.last() + it.substringAfterLast(" ").toInt())
        }
    }
    return result
}