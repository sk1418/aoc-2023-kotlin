// https://adventofcode.com/2023/day/9
fun main() {
    val today = "Day09"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun toIntLists(input: List<String>) = input.map { line -> line.split(" +".toRegex()).map { it.toInt() } }

    fun findPrediction1(ints: List<Int>): Int {
        return ints.last() + if (ints.any { it != 0 })
            findPrediction1(ints.windowed(size = 2) { it[1] - it[0] })
        else 0
    }

    fun findPrediction2(ints: List<Int>): Int {
        return ints.first() - if (ints.any { it != 0 })
            findPrediction2(ints.windowed(size = 2) { it[1] - it[0] })
        else 0
    }

    fun part1(input: List<String>): Int = toIntLists(input).sumOf { findPrediction1(it) }
    fun part2(input: List<String>): Int = toIntLists(input).sumOf { findPrediction2(it) }

    chkTestInput(part1(testInput), 114, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 2, Part2)
    println("[Part2]: ${part2(input)}")
}