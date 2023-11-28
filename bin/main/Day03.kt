// https://adventofcode.com/2023/day/3
fun main() {
    val today = "Day03"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return input.map { line ->
            (line.length / 2).let { size ->
                letterToPrio(line.substring(0, size).toSet().intersect(line.substring(size).toSet()).first())
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toSet() }.chunked(3).sumOf { l3 ->
            letterToPrio(l3[0].intersect(l3[1]).intersect(l3[2]).first())
        }
    }

    chkTestInput(part1(testInput), 157, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 70, Part2)
    println("[Part2]: ${part2(input)}")
}

fun letterToPrio(c: Char): Int = when (c) {
    in 'a'..'z' -> c.code - 96
    in 'A'..'Z' -> c.code - 38
    else -> 0
}
