// https://adventofcode.com/2023/day/11
fun main() {
    val today = "Day11"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        val mList = input.toMonkeyList()
        repeat(20) {
            mList.forEach { it.work(mList) { afterOp: Long -> afterOp / 3 } }
        }
        val num = mList.map { it.numOfItems }.sortedDescending()
        return num[0] * num[1]
    }

    fun part2(input: List<String>): Long {
        val mList = input.toMonkeyList()
        val byX = mList.fold(1) { acc, monkey -> acc * monkey.testDiv }
        repeat(10000) {
            mList.forEach { it.work(mList) { afterOp: Long -> afterOp % byX } }
        }
        val num = mList.map { it.numOfItems }.sortedDescending()
        return num[0] * num[1]
    }

    chkTestInput(part1(testInput), 10605L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 2713310158L, Part2)
    println("[Part2]: ${part2(input)}")
}

fun List<String>.toMonkeyList() = this.chunked(7).map { mlines ->
    val items = mlines[1].substringAfter(": ").split(", ").map { it.toLong() }.toMutableList()
    val (op, value) = mlines[2].split(" ").let { it[it.size - 2] to it.last() }
    val func = when (op) {
        "+" -> if (value == "old") { old: Long -> old + old } else { old: Long -> old + value.toLong() }
        "*" -> if (value == "old") { old: Long -> old * old } else { old: Long -> old * value.toLong() }
        else -> throw IllegalStateException("unknown operation")
    }
    Monkey(
        items = items,
        operation = func,
        testDiv = mlines[3].substringAfterLast(" ").toInt(),
        trueForwardIdx = mlines[4].substringAfterLast(" ").toInt(),
        falseForwardIdx = mlines[5].substringAfterLast(" ").toInt(),
    )
}

data class Monkey(
    val items: MutableList<Long>, val operation: (Long) -> Long, val testDiv: Int,
    val trueForwardIdx: Int, val falseForwardIdx: Int, var numOfItems: Long = 0,
) {
    fun work(monkeyList: List<Monkey>, postCalc: (Long) -> Long) {
        items.forEach {
            val newItem = postCalc(operation(it))
            if (newItem % testDiv == 0L) monkeyList[trueForwardIdx].items += newItem else monkeyList[falseForwardIdx].items += newItem
        }.also {
            numOfItems += items.size
            items.clear()
        }
    }
}