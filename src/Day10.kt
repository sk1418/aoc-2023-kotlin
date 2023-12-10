import Direction.Down
import Direction.Left
import Direction.Right
import Direction.Up

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
        return MatrixDay10(start, input[0].lastIndex, input.lastIndex, points)
    }

    fun part1(input: List<String>): Int {
        val matrix = toMatrix(input)
        return matrix.move().size / 2
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
        val loop = matrix.move().toSet()
        return (matrix.points.keys - loop).map { p ->
            var intersectCnt = 0
            var myX = p.first
            while (myX < matrix.maxX) {
                myX++
                (myX to p.second).also {
                    intersectCnt += if (it in loop && matrix.points[it]!! !in "L-J") 1 else 0
                }
            }
            intersectCnt % 2 == 1
        }.count { it }
    }

    chkTestInput(part1(testInput), 8, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInputPart2), 10, Part2)
    println("[Part2]: ${part2(input)}")
}

enum class Direction { Up, Down, Left, Right }


class MatrixDay10(val start: Pair<Int, Int>, val maxX: Int, val maxY: Int, val points: Map<Pair<Int, Int>, Char>) { //: Matrix<Char>(maxX, maxY, points) {
    private var current = start
    private var move: Direction
    val path = mutableListOf(start)

    init {
        move = if (points[current.left()]!! in "L-F") {
            Left
        } else if (points[current.right()]!! in "J-7") {
            Right
        } else if (points[current.up()]!! in "|F7") {
            Up
        } else if (points[current.down()]!! in "|JL") {
            Down
        } else {
            throw IllegalStateException("Invalid start point")
        }
        current = start.move(move).also { path += it }
    }

    fun move(): MutableList<Pair<Int, Int>> {
        while (points[current]!! != 'S') {
            current = toNext()
            path += current
        }
        return path
    }

    fun Pair<Int, Int>.up(): Pair<Int, Int> = (first to ((second - 1).takeIf { it >= 0 } ?: 0)).also { move = Up }
    fun Pair<Int, Int>.down(): Pair<Int, Int> = (first to ((second + 1).takeIf { it <= maxY } ?: maxY)).also { move = Down }
    fun Pair<Int, Int>.left(): Pair<Int, Int> = (((first - 1).takeIf { it >= 0 } ?: 0) to second).also { move = Left }
    fun Pair<Int, Int>.right(): Pair<Int, Int> = (((first + 1).takeIf { it <= maxX } ?: maxX) to second).also { move = Right }
    fun Pair<Int, Int>.move(direction: Direction): Pair<Int, Int> =
      when (direction) {
          Up -> up()
          Down -> down()
          Left -> left()
          Right -> right()
      }

    fun toNext(): Pair<Int, Int> {
        return with(current) {
            when (val curChar = points[current]!!) {
                'F' -> if (move == Left) down() else right()
                '7' -> if (move == Right) down() else left()
                'J' -> if (move == Right) up() else left()
                'L' -> if (move == Left) up() else right()
                '|' -> if (move == Up) up() else down()
                '-' -> if (move == Left) left() else right()
                else -> throw java.lang.IllegalStateException("cannot move further")
            }
        }
    }
}