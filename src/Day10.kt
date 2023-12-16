import Direction.*

// https://adventofcode.com/2023/day/10
fun main() {
    val today = "Day10"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val testInputPart2 = readTestInput("$today-part2")

    fun toMatrix(input: List<String>): MatrixDay10 {
        lateinit var start: Pair<Int, Int>
        val points = mutableMapOf<Pair<Int, Int>, Char>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                points.put(x to y, c).also {
                    if (c == 'S') {
                        start = x to y
                    }
                }
            }
        }
        return MatrixDay10(start, input[0].lastIndex, input.lastIndex, NotNullMap(points.toMap()))
    }

    fun part1(input: List<String>): Int {
        val matrix = toMatrix(input)
        return matrix.startMoving().size / 2
    }

    /**
     * https://en.wikipedia.org/wiki/Point_in_polygon
     * Ray casting algorithm
     *
     * One simple way of finding whether the point is inside or outside a simple polygon is
     * to test how many times a ray, starting from the point and going in any fixed direction,
     * intersects the edges of the polygon. If the point is on the outside of the polygon the ray
     * will intersect its edge an even number of times. If the point is on the inside of the
     * polygon then it will intersect the edge an odd number of times.
     * However, must exclude `L, -, and J` (or `F, -, and F`)
     *
     */
    fun part2(input: List<String>): Int {
        val matrix = toMatrix(input)
        val loop = matrix.startMoving().toSet()
        return (matrix.points.keys - loop).map { p ->
            var intersectCnt = 0
            var myX = p.first
            while (myX < matrix.maxX) {
                myX++
                intersectCnt += (myX to p.second).let { if (it in loop && matrix.points[it] !in "L-J") 1 else 0 }
            }
            intersectCnt % 2 == 1
        }.count { it }
    }

    chkTestInput(part1(testInput), 8, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInputPart2), 10, Part2)
    println("[Part2]: ${part2(input)}")
}

class MatrixDay10(start: Pair<Int, Int>, maxX: Int, maxY: Int, override val points: NotNullMap<Pair<Int, Int>, Char>) : Matrix<Char>(maxX, maxY, points) {
    private lateinit var move: Direction
    private val loop = mutableListOf(start)
    private var current = (if (points[movePoint(Left, start)] in "L-F") {
        movePoint(Left, start)
    } else if (points[movePoint(Right, start)] in "J-7") {
        movePoint(Right, start)
    } else if (points[movePoint(Up, start)] in "|F7") {
        movePoint(Up, start)
    } else if (points[movePoint(Down, start)] in "|JL") {
        movePoint(Down, start)
    } else {
        throw IllegalStateException("Invalid start point")
    }).also {
        loop += it
    }

    fun movePoint(direction: Direction, point: Pair<Int, Int> = current): Pair<Int, Int> = point.safeMove(direction).also { move = direction }

    fun startMoving(): MutableList<Pair<Int, Int>> {
        while (points[current] != 'S') {
            current = next()
            loop += current
        }
        return loop
    }

    fun next(): Pair<Int, Int> {
        return when (points[current]) {
            'F' -> if (move == Left) movePoint(Down) else movePoint(Right)
            '7' -> if (move == Right) movePoint(Down) else movePoint(Left)
            'J' -> if (move == Right) movePoint(Up) else movePoint(Left)
            'L' -> if (move == Left) movePoint(Up) else movePoint(Right)
            '|' -> if (move == Up) movePoint(move) else movePoint(Down)
            '-' -> if (move == Left) movePoint(move) else movePoint(Right)
            else -> throw java.lang.IllegalStateException("cannot move further")
        }
    }
}