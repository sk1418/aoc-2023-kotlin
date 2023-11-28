import kotlin.math.abs

// https://adventofcode.com/2023/day/15
fun main() {
    val today = "Day15"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>, col:Int): Int {
        val sensors = toSensors(input)
        val xMin = sensors.minOf { minOf(it.pos.x, it.beacon.x, it.pos.x - it.coverage()) }
        val xMax = sensors.maxOf { maxOf(it.pos.x, it.beacon.x, it.pos.x + it.coverage()) }
        return (xMin..xMax).count { x ->
            Pos(x,col).let { p -> sensors.any { s -> s.beacon != p && s.pos.distance(p) <= s.coverage() } }
        }
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    chkTestInput(part1(testInput,10), 26, Part1)
    println("[Part1]: ${part1(input,2000000)}")

    chkTestInput(part2(testInput), 0L, Part2)
    println("[Part2]: ${part2(input)}")
}

data class Pos(val x: Int, val y: Int) {
    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)

    fun distance(other: Pos) = abs(other.x - x) + abs(other.y - y)
}

data class Sensor(val pos: Pos, val beacon: Pos) {
    fun coverage() = pos.distance(beacon)
}

fun toSensors(input: List<String>): List<Sensor> {
//    Sensor at x=2, y=18: closest beacon is at x=-2, y=15
   return input.map {
        it.split("x=|, ".toRegex()).let { listOf(it[1].toInt(), it[3].toInt()) }
            .zip(it.split("y=|: ".toRegex()).let { listOf(it[1].toInt(), it[3].toInt()) })
            .let { twoPairs ->
                Sensor(Pos(twoPairs[0]), Pos(twoPairs[1]))
            }
    }
}