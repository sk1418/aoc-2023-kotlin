// https://adventofcode.com/2023/day/3
val numberRE = "\\d+".toRegex()
val starRE = "[*]".toRegex()
fun main() {
    val today = "Day03"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEachIndexed { row, line ->
            numberRE.findAll(line).forEach { match ->
                val n = match.value.toInt()
                if (adjacentToSymbol(input, row, match.range.first, match.range.count())) { sum += n }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        input.forEachIndexed { row, line ->
            starRE.findAll(line).forEach { star ->
                val starCol = star.range.first
                sum += calcGearRatio((row to starCol), input)
            }
        }
        return sum
    }

    chkTestInput(part1(testInput), 4361, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 467835, Part2)
    println("[Part2]: ${part2(input)}")
}

fun subListAdjacentToSymbol(matrix: List<String>, row: Int, col: Int, len: Int): Boolean {
    val colStart = if (col >= 1) col - 1 else col
    return if (row >= 0 && row < matrix.size) {
        val colEnd = if (col + len < matrix[row].lastIndex) col + len + 1 else col + len
        matrix[row].subSequence(colStart, colEnd).any { c -> !(c.isDigit() || c == '.') }
    } else false
}

fun adjacentToSymbol(matrix: List<String>, row: Int, col: Int, len: Int): Boolean {
    return sequenceOf(
        subListAdjacentToSymbol(matrix, row - 1, col, len),
        subListAdjacentToSymbol(matrix, row, col, len),
        subListAdjacentToSymbol(matrix, row + 1, col, len),
    ).any { it }
}

// part2
fun calcGearRatio(starPos: Pair<Int, Int>, matrix: List<String>): Int {

    fun Sequence<MatchResult>.numbersCoverStar(starRange: IntRange) = filter { starRange.intersect(it.range).isNotEmpty() }.map { it.value.toInt() }

    val (row, col) = starPos
    val starRange = (if (col == 0) 0 else col - 1)..(if (col == matrix[row].lastIndex) col else col + 1)
    val effectiveNums = listOf(
        (if (row == 0) emptySequence() else numberRE.findAll(matrix[row - 1])).numbersCoverStar(starRange), //above
        numberRE.findAll(matrix[row]).numbersCoverStar(starRange), //middle
        if (row == matrix.lastIndex) emptySequence() else numberRE.findAll(matrix[row + 1]).numbersCoverStar(starRange),//below
    ).flatMap { it }
    return if (effectiveNums.size != 2) 0 else effectiveNums.let { it.first() * it[1] }
}