// https://adventofcode.com/2023/day/4
fun main() {
    val today = "Day04"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun splitCards(puzzle: List<String>): List<Pair<List<String>, List<String>>> {
        return puzzle.map { line ->
            line.split(": *| *[|] *".toRegex()).let {
                it[1].split(" +".toRegex()) to it[2].split(" +".toRegex())
            }
        }
    }

    fun part1(input: List<String>): Int {
        return splitCards(input).sumOf {
            (it.first.intersect(it.second.toSet()).size.let { w -> if (w > 0) 1 shl (w - 1) else 0 })
        }
    }

    fun part2(input: List<String>): Int {
        val scores = splitCards(input).map { it.first.intersect(it.second.toSet()).size }
        val counts = MutableList(input.size) { 1 }
        scores.forEachIndexed { i, w -> (i + 1..i + w).forEach { counts[it] += counts[i] } }
        return counts.sum()
    }

    chkTestInput(part1(testInput), 13, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 30, Part2)
    println("[Part2]: ${part2(input)}")
}