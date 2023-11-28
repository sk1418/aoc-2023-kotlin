// https://adventofcode.com/2023/day/6
fun main() {
    val today = "Day06"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return findDistinctWindow(input.first(),4)
    }

    fun part2(input: List<String>): Int {
        return findDistinctWindow(input.first(),14)
    }

    chkTestInput(part1(testInput), 7, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 19, Part2)
    println("[Part2]: ${part2(input)}")
}

fun findDistinctWindow(input: String, wSize: Int) =
    input.toList().windowed(wSize).indexOfFirst { it.toSet().size == wSize } + wSize
