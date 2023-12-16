import Direction.*

// https://adventofcode.com/2023/day/16
fun main() {
    val today = "Day16"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun toMatrix(input: List<String>): MatrixDay16 {
        val points = mutableMapOf<Pair<Int, Int>, Char>()
        input.forEachIndexed { y, line -> line.forEachIndexed { x, c -> points[x to y] = c } }
        return MatrixDay16(input[0].lastIndex, input.lastIndex, NotNullMap(points.toMap()))
    }

    fun part1(input: List<String>): Int {
        return toMatrix(input).start(Pair(0, 0), Right)
    }

    fun part2(input: List<String>): Int {
        val matrix = toMatrix(input)
        val maxX = matrix.maxX
        val maxY = matrix.maxY

        return maxOf(
            (0..maxX).maxOf { maxOf(matrix.start(it to 0, Down), matrix.start(it to maxY, Up)) },
            (0..maxY).maxOf { maxOf(matrix.start(0 to it, Right), matrix.start(maxX to it, Left)) }
        )
    }

    chkTestInput(part1(testInput), 46, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 51, Part2)
    println("[Part2]: ${part2(input)}")
}

class MatrixDay16(maxX: Int, maxY: Int, points: NotNullMap<Pair<Int, Int>, Char>) : Matrix<Char>(maxX, maxY, points) {
    val visitedPD = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()

    fun start(from: Pair<Int, Int>, direction: Direction): Int {
        fire(from, direction)
        return visitedPD.map { (p, _) -> p }.toSet().size.also { visitedPD.clear() }
    }

    private fun fire(point: Pair<Int, Int>, direction: Direction) {
        if (point !in points.keys) return
        val key = point to direction
        if (key in visitedPD) return
        visitedPD += key

        when (points[point]) {
            '.' -> fire(point.move(direction), direction)
            '-' -> if (direction.horizontal()) {
                fire(point.move(direction), direction)
            } else {
                fire(point.move(Left), Left)
                fire(point.move(Right), Right)
            }

            '|' -> if (direction.horizontal()) {
                fire(point.move(Up), Up)
                fire(point.move(Down), Down)
            } else {
                fire(point.move(direction), direction)
            }

            '/' -> (if (direction.horizontal()) direction.turn90Back() else direction.turn90()).also { newDir ->
                fire(point.move(newDir), newDir)
            }

            '\\' -> (if (direction.horizontal()) direction.turn90() else direction.turn90Back()).also { newDir ->
                fire(point.move(newDir), newDir)
            }
        }
    }

    private fun Direction.horizontal() = this == Left || this == Right
}