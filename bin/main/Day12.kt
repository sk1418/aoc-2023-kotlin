// https://adventofcode.com/2023/day/12
fun main() {
    val today = "Day12"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return toGraph(input).also { it.traverseFrom(it.start) }.end.minStepsToMe
    }

    fun part2(input: List<String>): Int {
        val g = toGraph(input)
        val possibleStart = listOf(g.start) + g.points.flatten().filter { it.v == 'a' }
        val steps = possibleStart.map { theStart ->
            g.run {
                initGraph(theStart)
                traverseFrom(theStart)
                end.minStepsToMe
            }
        }
        return steps.minOf { it }
    }

    chkTestInput(part1(testInput), 31, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 29, Part2)
    println("[Part2]: ${part2(input)}")
}

fun toGraph(input: List<String>): Graph {
    lateinit var start: Point
    lateinit var end: Point
    return Graph(input.mapIndexed { x, line ->
        line.toCharArray().mapIndexed { y, c ->
            when (c) {
                'S' -> Point(x, y, 'a' - 1, 0).also { start = it }
                'E' -> Point(x, y, 'z' + 1).also { end = it }
                else -> Point(x, y, c)
            }
        }
    }, start, end)
}

data class Point(val x: Int, val y: Int, val v: Char, var minStepsToMe: Int = Int.MAX_VALUE)

data class Graph(val points: List<List<Point>>, var start: Point, val end: Point) {
    private val xRange = 0..points.lastIndex
    private val yRange = 0..points[0].lastIndex

    fun initGraph(newStart: Point) {
        points.forEach { it.forEach { p -> p.minStepsToMe = Int.MAX_VALUE } }
        start = newStart.also { it.minStepsToMe = 0 }
    }

    fun traverseFrom(point: Point) {
        findMovableFrom(point).forEach {
            traverseFrom(it)
        }
    }

    private fun findMovableFrom(ref: Point): List<Point> {
        val result = mutableListOf<Point>()
        if (ref.x + 1 in xRange) {
            addToMoveList(ref.x + 1, ref.y, ref, result)
        }
        if (ref.x - 1 in xRange) {
            addToMoveList(ref.x - 1, ref.y, ref, result)
        }
        if (ref.y - 1 in yRange) {
            addToMoveList(ref.x, ref.y - 1, ref, result)
        }
        if (ref.y + 1 in yRange) {
            addToMoveList(ref.x, ref.y + 1, ref, result)
        }
        return result
    }

    private fun addToMoveList(x: Int, y: Int, ref: Point, moveList: MutableList<Point>) {
        val p = points[x][y]
        if (p.v <= ref.v + 1 && p.minStepsToMe > ref.minStepsToMe + 1) {
            moveList += p.also { it.minStepsToMe = ref.minStepsToMe + 1 }
        }
    }
}