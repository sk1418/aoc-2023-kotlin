// https://adventofcode.com/2023/day/6
fun main() {
    val today = "Day06"

    val input = readInput(today)
    val testInput = readTestInput(today)

    val re = "\\D+".toRegex()

    fun part1(input: List<String>): Long {
        val races = input.map { it.split(re).drop(1).map { it.toInt() } }.let { it[0].zip(it[1]) }
        return races.map { (time, distance) ->
            val min = (1..time).first { (time - it) * it > distance }
//            val max = time - (2..time).first { ((distance) / it) < (time - it) }
            val max = time - min
            (max - min + 1).toLong().also { println("($time, $distance)-> Min Hold:$min, Max Hold: $max") }
        }.fold(1) { acc, e -> e * acc }
    }

    fun part2(input: List<String>): Long {
        val race = input.map { it.replace(re, "").toLong() }
        val (time, distance) = race[0] to race[1]
        val min = (1..time).first { (time - it) * it > distance }
//        val max = time - (2..time).first { ((distance) / it) < (time - it) }
        val max = time - min
        return (max - min + 1).also { println("($time, $distance)-> Min Hold:$min, Max Hold: $max") }
    }

    chkTestInput(part1(testInput), 288L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 71503L, Part2)
    println("[Part2]: ${part2(input)}")
}