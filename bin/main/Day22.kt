// https://adventofcode.com/2023/day/22
fun main() {
    val today = "Day22"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        val game = initGame(input)
        game.start()
        return game.run {
            1000 * curPos.row + 4 * curPos.col + when (curDir) {
                "R" -> 0
                "D" -> 1
                "L" -> 2
                "U" -> 3
                else -> throw IllegalStateException()
            }
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    chkTestInput(part1(testInput), 6032, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 0L, Part2)
    println("[Part2]: ${part2(input)}")
}

private fun initGame(input: List<String>): Day22Game {
    val pSet = mutableSetOf<PuzzelPos>()
    input.takeWhile { it.isNotBlank() }.forEachIndexed { r, s ->
        s.toList().forEachIndexed { c, v ->
            if (!v.isWhitespace()) pSet += PuzzelPos(r + 1, c + 1, v == '#')
        }
    }
    val instruction = input.last().split(Regex("(?=[LR])|(?<=[LR])")).toList()
    val curPos = pSet.filter { it.row == 1 }.minByOrNull { it.col }!!
    return Day22Game(pSet, curPos, "R", instruction)
}

data class PuzzelPos(val row: Int, val col: Int, var isRock: Boolean = false)
data class Day22Game(val pSet: Set<PuzzelPos>, var curPos: PuzzelPos, var curDir: String = "R", val instruction: List<String>) {
    private val rangeInRow = pSet.groupBy { it.row }.mapValues { it.value.minOf { it.col }..it.value.maxOf { it.col } }
    private val rangeInCol = pSet.groupBy { it.col }.mapValues { it.value.minOf { it.row }..it.value.maxOf { it.row } }

    fun start() {
        instruction.forEach { ins ->
            if (ins[0].isDigit()) {
                repeat(ins.toInt()) { move1Step() }
            } else {
                turn(ins)
            }
        }
    }

    private fun turn(ins: String) {
        curDir = when (ins) {
            "L" -> when (curDir) {
                "L" -> "D"
                "R" -> "U"
                "U" -> "L"
                "D" -> "R"
                else -> throw IllegalStateException()
            }
            "R" -> when (curDir) {
                "L" -> "U"
                "R" -> "D"
                "U" -> "R"
                "D" -> "L"
                else -> throw IllegalStateException()
            }
            else -> throw IllegalStateException()
        }
    }

    fun move1Step() {
        when (curDir) {
            "L", "R" -> horizontalMV(curDir)
            "U", "D" -> verticalMV(curDir)
            else -> throw IllegalStateException()
        }
    }

    private fun horizontalMV(dir: String) {
        val possibleCol = curPos.col + if (dir == "L") -1 else 1
        val newCol = if (possibleCol in rangeInRow[curPos.row]!!) possibleCol
        else rangeInRow[curPos.row]!!.let { if (dir == "L") it.last else it.first }
        pSet.first { it.row == curPos.row && it.col == newCol }.also {
            if (!it.isRock) curPos = it
        }
    }

    private fun verticalMV(dir: String) {
        val possibleRow = curPos.row + if (dir == "U") -1 else 1
        val newRow = if (possibleRow in rangeInCol[curPos.col]!!) possibleRow
        else rangeInCol[curPos.col]!!.let { if (dir == "U") it.last else it.first }
        pSet.first { it.col == curPos.col && it.row == newRow }.also {
            if (!it.isRock) curPos = it
        }
    }
}