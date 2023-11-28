// https://adventofcode.com/2023/day/9
fun main() {
    val today = "Day09"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val testInput2 = readTestInput("$today-part2")

    fun part1(input: List<String>): Int {
        val head = Knot(Point09(0, 0))
        val tail = Knot(Point09(0, 0))
        toMoves(input).forEach { m ->
            head.mv(m)
            tail.follow(head)
        }
        return tail.mvHist.toSet().size
    }

    fun part2(input: List<String>): Int {
        val knots = (1..10).map { Knot(Point09(0, 0)) }
        toMoves(input).forEach { m ->
            knots[0].mv(m)
            for (i in 1..9) {
                knots[i].follow(knots[i - 1])
            }
        }
        return knots[9].mvHist.toSet().size
    }

    chkTestInput(part1(testInput), 13, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput2), 36, Part2)
    println("[Part2]: ${part2(input)}")
}

fun toMoves(input: List<String>): MutableList<String> {
    val moves = mutableListOf<String>()
    input.forEach { it.split(" ").apply { repeat(this[1].toInt()) { moves.add(this[0]) } } }
    return moves
}

data class Knot(var point: Point09) {
    val mvHist: MutableList<Point09> = mutableListOf(point)
    fun mv(direction: String) {
        point = when (direction) {
            "L" -> point.left()
            "R" -> point.right()
            "D" -> point.down()
            "U" -> point.up()
            else -> point
        }.also { mvHist += it }
    }

    fun follow(head: Knot) = when {
        head.point.x == point.x && head.point.y - point.y > 1 -> mv("U")
        head.point.x == point.x && head.point.y - point.y < -1 -> mv("D")
        head.point.y == point.y && head.point.x - point.x > 1 -> mv("R")
        head.point.y == point.y && head.point.x - point.x < -1 -> mv("L")
        head.point.y != point.y && head.point.x != point.x
            && (kotlin.math.abs(head.point.x - point.x) > 1 || kotlin.math.abs(head.point.y - point.y) > 1) -> {
            val newX = point.x + if (head.point.x > point.x) 1 else -1
            val newY = point.y + if (head.point.y > point.y) 1 else -1
            point = Point09(newX, newY).also { mvHist += it }
        }
        else -> {}
    }
}


data class Point09(val x: Int, val y: Int) {
    fun right() = Point09(x + 1, y)
    fun left() = Point09(x - 1, y)
    fun down() = Point09(x, y - 1)
    fun up() = Point09(x, y + 1)
}
