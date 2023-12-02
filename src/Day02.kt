// https://adventofcode.com/2023/day/2
fun main() {
    val today = "Day02"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14
        return input.map { line -> GameDay2.buildByLine(line) }.filter { game ->
            game.blues.max() <= maxBlue && game.reds.max() <= maxRed && game.greens.max() <= maxGreen
        }.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line -> GameDay2.buildByLine(line).power() }
    }


    chkTestInput(part1(testInput), 8, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 2286, Part2)
    println("[Part2]: ${part2(input)}")
}

data class GameDay2(
    val id: Int,
    val reds: MutableList<Int>, val greens: MutableList<Int>, val blues: MutableList<Int>
) {
    fun power(): Int = reds.max() * greens.max() * blues.max()

    companion object {
        private val splitReg = """[:,;]? """.toRegex()
        fun buildByLine(line: String): GameDay2 {
            val strList = line.split(splitReg).reversed()
            val id = strList[strList.lastIndex - 1].toInt()
            val reds: MutableList<Int> = mutableListOf()
            val greens: MutableList<Int> = mutableListOf()
            val blues: MutableList<Int> = mutableListOf()
            strList.forEachIndexed { idx, s ->
                when (s) {
                    "blue" -> blues += strList[idx + 1].toInt()
                    "red" -> reds += strList[idx + 1].toInt()
                    "green" -> greens += strList[idx + 1].toInt()
                }
            }
            return GameDay2(id, reds, greens, blues)
        }
    }
}