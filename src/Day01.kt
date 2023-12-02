// https://adventofcode.com/2023/day/1
fun main() {
    val today = "Day01"
    val part2 = "$today-part2"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val testPart2 = readTestInput(part2)


    fun part1(input: List<String>): Long {
        return input.sumOf {
            it.filter { c -> c.isDigit() }.let { s -> "${s.first()}${s.last()}".toLong() }
        }
    }

    fun part2(input: List<String>): Long {
        val repMap = mapOf(
            "one" to "o1e",
            "two" to "t2o",
            "three" to "t3e",
            "four" to "f4r",
            "five" to "f5e",
            "six" to "s6x",
            "seven" to "s7n",
            "eight" to "e8t",
            "nine" to "n9e"
        )

        return input.sumOf { line ->
            var myLine = line
            repMap.forEach { (k, v) -> myLine = myLine.replace(k, v) }
            myLine.filter { c -> c.isDigit() }.let { s -> "${s.first()}${s.last()}".toLong() }
        }
    }

    chkTestInput(part1(testInput), 142L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testPart2), 281L, Part2)
    println("[Part2]: ${part2(input)}")
}