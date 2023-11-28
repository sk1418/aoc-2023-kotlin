import kotlin.math.max

fun main() {
    val today = "Day01"
    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(lines: List<String>): Int {
        var max = 0
        var curSum = 0

        lines.forEach {
            it.ifBlank {
                max = max(max, curSum)
                curSum = 0
                return@forEach
            }
            curSum += it.toInt()
        }
        return max
    }

    fun part2(lines: List<String>): Int {
        val sumList: MutableList<Int> = mutableListOf()
        var curSum = 0

        lines.forEach {
            it.ifBlank {
                sumList += curSum
                curSum = 0
                return@forEach
            }
            curSum += it.toInt()
        }
        sumList += curSum
        return sumList.apply { sortDescending() }.take(3).sum()
    }

    chkTestInput(part1(testInput), 24000, Part1)
    println("$Part1: ${part1(input)}")

    chkTestInput(part2(testInput), 45000, Part2)
    println("$Part2: ${part2(input)}")
}
