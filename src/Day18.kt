import Direction.*

// https://adventofcode.com/2023/day/18
fun main() {
    val today = "Day18"
    fun toPlans(input: List<String>) =
        input.map { it.split("[ ()]+".toRegex()).let { Plan(it[0], it[1].toInt(), it[2]) } }


    val input = readInput(today)
    val testInput = readTestInput(today)

    fun Pair<Int, Int>.move(direction: Direction, steps: Int) = when (direction) {
        Up -> first to second - steps
        Down -> first to second + steps
        Left -> first - steps to second
        Right -> first + steps to second
    }

    fun part1(input: List<String>): Int {
        var cur = 0 to 0
        val plans = toPlans(input)
        val points = buildList {
            add(cur)
            plans.forEach { plan ->
                cur = cur.move(plan.direction, plan.step)
                add(cur)
            }
        }
        //https://en.wikipedia.org/wiki/Shoelace_formula
        return points.windowed(2).sumOf { twoPos ->
            val (x1, y1) = twoPos.first()
            val (x2, y2) = twoPos.last()
            x1 * y2 - x2 * y1
        } / 2 + plans.sumOf { it.step } / 2 /*border size*/ + 1
    }

    fun part2(input: List<String>): Long {
        var cur = 0 to 0
        val plans = toPlans(input)
        val points = buildList {
            add(cur)
            plans.forEach { plan ->
                cur = cur.move(plan.part2Direction, plan.part2Distance)
                add(cur)
            }
        }
        //https://en.wikipedia.org/wiki/Shoelace_formula
        return points.windowed(2).sumOf { twoPos ->
            val (x1, y1) = twoPos.first()
            val (x2, y2) = twoPos.last()
            x1.toLong() * y2 - x2.toLong() * y1
        } / 2 + plans.sumOf { it.part2Distance } / 2 /*border size*/ + 1
    }

    chkTestInput(part1(testInput), 62, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 952408144115L, Part2)
    println("[Part2]: ${part2(input)}")
}

data class Plan(val d: String, val step: Int, val color: String) {
    val direction = when (d) {
        "R" -> Right
        "L" -> Left
        "U" -> Up
        "D" -> Down
        else -> error("won't happen")
    }

    val part2Distance = color.drop(1).dropLast(1).toInt(16)
    val part2Direction = listOf(Right, Down, Left, Up)[color.last().digitToInt()]
}