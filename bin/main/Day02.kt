// https://adventofcode.com/2023/day/2


fun main() {
    val today = "Day02"

    val input = readInput(today).map { val r = it.split(" "); r[0] to r[1] }
    val testInput = readTestInput(today).map { val r = it.split(" "); r[0] to r[1] }


    fun part1(input: List<Pair<String, String>>): Int {
        return input.sumOf { scoreByShapes(it.second, it.first) }
    }

    fun part2(input: List<Pair<String, String>>): Int {
        return input.sumOf { scoreByResult(it.first, it.second) }
    }

    chkTestInput(part1(testInput), 15, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 12, Part2)
    println("[Part2]: ${part2(input)}")
}

val oppMap = mapOf("A" to 1, "B" to 2, "C" to 3)
val myMap = mapOf("X" to 1, "Y" to 2, "Z" to 3)

fun scoreByShapes(mine: String, opp: String): Int {
    val myV = myMap[mine]!!
    val oppV = oppMap[opp]!!
    return myV + when {
        myV == oppV -> 3 // draw
        (myV - 1).let { it == oppV || it + 3 == oppV } -> 6 // win (it +3 ==oppV, in case it==0)
        else -> 0 //lose
    }
}

fun scoreByResult(opp: String, result: String): Int {
    val oppV = oppMap[opp]!!
    return when (result) {
        "X" -> 0 + if (oppV - 1 == 0) 3 else oppV - 1 // to lose
        "Y" -> 3 + oppV// to draw
        else -> 6 + if (oppV + 1 == 4) 1 else oppV + 1 //to win
    }
}
