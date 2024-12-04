import Direction.*
import kotlin.math.abs

// https://adventofcode.com/2023/day/18
fun main() {
    val today = "Day18"
    fun toPlans(input: List<String>) =
        input.map { it.split("[ ()]+".toRegex()).let { Plan(it[0], it[1].toInt(), it[2]) } }

    fun toMatrix(input: List<String>): MatrixDay18 {
        val points = mutableMapOf<Pair<Int, Int>, Char>()
        input.forEachIndexed { y, line -> line.forEachIndexed { x, c -> points[x to y] = c } }
        return MatrixDay18(input[0].lastIndex, input.lastIndex, NotNullMap(points.toMap()))
    }

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun Pair<Int, Int>.move(direction: Direction, steps: Int) = when (direction) {
            Up -> (second - steps..second).map { first to it }
            Down -> (second..second + steps).map { first to it }
            Left -> (first - steps..first).map { it to second }
            Right -> (first..first + steps).map { it to second }
    }

    fun part1(input: List<String>): Int {
        val plans = toPlans(input)
        var maxX = 0
        var minX = 0
        var maxY = 0
        var minY = 0
        val points = mutableSetOf(0 to 0)
        plans.fold(0 to 0) { acc, plan ->
            acc.move(plan.direction, plan.value).let {
                points.addAll( it)
                maxX = maxOf(maxX, it.first().first, it.last().first)
                maxX = minOf(minX, it.first().first, it.last().first)
                maxY = maxOf(maxY, it.first().second, it.last().second)
                minY = minOf(minY, it.first().second, it.last().second)
                when (plan.direction) {
                    Up, Left -> it.first()
                    Down, Right-> it.last()
                }
            }
        }

        return (minY..maxY).sumOf { y ->
            points.filter { it.second == y }
                .sortedBy { it.first }.let { abs(it.last().first - it.first().first) + 1 }
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    chkTestInput(part1(testInput), 62, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 0, Part2)
    println("[Part2]: ${part2(input)}")
}

class Plan(d: String, val value: Int, val color: String) {
    val direction = when (d) {
        "R" -> Right
        "L" -> Left
        "U" -> Up
        "D" -> Down
        else -> throw IllegalArgumentException("XXX")
    }
}

class MatrixDay18(maxX: Int, maxY: Int, points: Map<Pair<Int, Int>, Char>) : Matrix<Char>(maxX, maxY, points) {
    fun walk(point: Pair<Int, Int>, direction: Direction) {

    }
}