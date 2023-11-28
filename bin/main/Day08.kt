import kotlin.math.max

// https://adventofcode.com/2023/day/8
fun main() {
    val today = "Day08"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return toMatrix(input).findVisible()
    }

    fun part2(input: List<String>): Int {
        return toMatrix(input).findHighestScore()
    }

    chkTestInput(part1(testInput), 21, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 8, Part2)
    println("[Part2]: ${part2(input)}")
}

fun toMatrix(input: List<String>): List<List<Int>> = input.map { line -> line.toList().map { it.digitToInt() } }

fun List<List<Int>>.findVisible(): Int {
    var innerCnt = 0
    for (i in 1 until this[0].lastIndex) {
        for (j in 1 until this.lastIndex) {
            innerCnt += (if (visible(this, i, j)) 1 else 0)
        }
    }
    return innerCnt + this[0].size * 2 + this.size * 2 - 4
}

fun visible(m: List<List<Int>>, x: Int, y: Int) =
    (0 until x).all { m[it][y] < m[x][y] } ||
        (x + 1 until m[0].size).all { m[it][y] < m[x][y] } ||
        (0 until y).all { m[x][it] < m[x][y] } ||
        (y + 1 until m.size).all { m[x][it] < m[x][y] }

fun List<List<Int>>.findHighestScore(): Int {
    var highScore = 0
    for (i in 1 until this[0].lastIndex) {
        for (j in 1 until this.lastIndex) {
            highScore = max(calcScore(this, i, j), highScore)
        }
    }
    return highScore
}

fun calcScore(m: List<List<Int>>, x: Int, y: Int) =
    ((x - 1 downTo 0).indexOfFirst { m[it][y] >= m[x][y] }.let { if (it < 0) x else (it + 1) }) *
        ((x + 1..m[0].lastIndex).indexOfFirst { m[it][y] >= m[x][y] }.let { if (it < 0) m[0].lastIndex - x else (1 + it) }) *
        ((y - 1 downTo 0).indexOfFirst { m[x][it] >= m[x][y] }.let { if (it < 0) y else (it + 1) }) *
        ((y + 1..m.lastIndex).indexOfFirst { m[x][it] >= m[x][y] }.let { if (it < 0) m.lastIndex - y else (it + 1) })
