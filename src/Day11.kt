import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2023/day/11
fun main() {
    val today = "Day11"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun calcLengths(input: List<String>, expansionRate: Int = 2): Long {
        val emptyRows = mutableListOf<Int>()
        val emptyCols = mutableListOf<Int>()

        val matrix = input.map { it.toList() }
        matrix.forEachIndexed { i, row -> if ('#' !in row) emptyRows += i }
        matrix.transpose().forEachIndexed { i, chars -> if ('#' !in chars) emptyCols += i }

        val galaxies = mutableListOf<Pair<Int, Int>>()
        matrix.forEachIndexed { y, line -> line.forEachIndexed { x, c -> if (c == '#') galaxies += x to y } }
        val distances = mutableMapOf<PointPair, Long>()

        galaxies.forEachIndexed { i, p1 ->
            for (j in min((i + 1), galaxies.lastIndex)..galaxies.lastIndex) {
                val p2 = galaxies[j]
                //calc the expansion counts
                val colRange = min(p1.first, p2.first)..max(p1.first, p2.first)
                val rowRange = min(p1.second, p2.second)..max(p1.second, p2.second)
                val (expRow, expCol) = emptyRows.count { it in rowRange } to emptyCols.count { it in colRange }
                distances[PointPair(p1, p2)] = abs(p1.first - p2.first) + abs(p1.second - p2.second) +
                  expRow * (expansionRate - 1).toLong() + expCol * (expansionRate - 1).toLong()
            }
        }
        return distances.values.sum()
    }

    fun part1(input: List<String>): Long {
        return calcLengths(input)
    }

    fun part2(input: List<String>): Long {
        return calcLengths(input, 1000000)
    }

    chkTestInput(part1(testInput), 374L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput),82000210L, Part2)
    println("[Part2]: ${part2(input)}")
}

class PointPair(val pair1: Pair<Int, Int>, val pair2: Pair<Int, Int>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PointPair) return false

        if (pair1 == other.pair1 && pair2 == other.pair2) return true
        if (pair1 == other.pair2 && pair2 == other.pair1) return true

        return false
    }

    override fun hashCode(): Int {
        var result = pair1.hashCode()
        result = 31 * result + pair2.hashCode()
        return result
    }
}