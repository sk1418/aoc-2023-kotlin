// https://adventofcode.com/2023/day/4
fun main() {
    val today = "Day04"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return input.map { lineToRangePair(it) }.count {
            it.first.containsAll(it.second) || it.second.containsAll(it.first)
        }
    }

    fun part2(input: List<String>): Int {
        return input.map { lineToRangePair(it) }.count {
            it.first.intersect(it.second).isNotEmpty()
        }
    }

    chkTestInput(part1(testInput), 2, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 4, Part2)
    println("[Part2]: ${part2(input)}")
}

fun lineToRangePair(line: String): Pair<List<Int>, List<Int>> {
    val sec = line.split(",")
    return Pair(sec[0].split("-").let { (it[0].toInt()..it[1].toInt()).toList() },
        sec[1].split("-").let { (it[0].toInt()..it[1].toInt()).toList() })
}
