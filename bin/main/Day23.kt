// https://adventofcode.com/2023/day/23
fun main() {
    val today = "Day23"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        val game = initGame(input)
        repeat(10) { game.round() }
        return game.findEmptyPos()
    }

    fun part2(input: List<String>): Int {
        return initGame(input).start()
    }

    chkTestInput(part1(testInput), 110, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 20, Part2)
    println("[Part2]: ${part2(input)}")
}

private fun initGame(input: List<String>): Day23Game {
    val pList = mutableSetOf<ElfPos>()
    input.forEachIndexed { r, s ->
        s.toList().forEachIndexed { c, v ->
            if (v == '#') pList += ElfPos(r, c)
        }
    }
    return Day23Game(pList)
}

data class ElfPos(var row: Int, var col: Int)
data class Day23Game(val posSet: Set<ElfPos>) {
    private val moveMap = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()

    private val nFree = { p: ElfPos -> p.hasNorthAny().not() }
    private val sFree = { p: ElfPos -> p.hasSouthAny().not() }
    private val wFree = { p: ElfPos -> p.hasWestAny().not() }
    private val eFree = { p: ElfPos -> p.hasEastAny().not() }
    private val ruleList = ArrayDeque(listOf(nFree, sFree, wFree, eFree))
    private val ruleMap = mapOf(
        nFree to { p: ElfPos -> p.row - 1 to p.col },
        sFree to { p: ElfPos -> p.row + 1 to p.col },
        wFree to { p: ElfPos -> p.row to p.col - 1 },
        eFree to { p: ElfPos -> p.row to p.col + 1 },
    )

    fun round(): Boolean {
        prepare()
        return move().also {
            ruleList.addLast(ruleList.removeFirst())
        }
    }

    fun start(): Int {
        var i = 1
        while (round()) {
            i++
        }
        return i
    }

    fun findEmptyPos() = posSet.run {
        (maxOf { it.row } - minOf { it.row } + 1) * (maxOf { it.col } - minOf { it.col } + 1) - size
    }

    private fun prepare() {
        posSet.forEach { p ->
            moveMap[p.row to p.col] = p.row to p.col
            if (p.isNotAlone()) {
                moveMap[p.row to p.col] = ruleList.firstOrNull { it(p) }?.let { ruleMap[it]!!(p) } ?: (p.row to p.col)
            }
        }
    }

    private fun move(): Boolean {
        val r = moveMap.any { (k, v) -> k != v }
        val pairs = moveMap.values
        posSet.forEach { p ->
            val pair = moveMap[p.row to p.col]!!
            if (pairs.count { it == pair } == 1) {
                with(p) {
                    row = pair.first
                    col = pair.second
                }
            }
        }
        return r.also { moveMap.clear() }
    }

    fun ElfPos.hasNorthAny() = posSet.any { it.row == row - 1 && it.col in col - 1..col + 1 }
    fun ElfPos.hasSouthAny() = posSet.any { it.row == row + 1 && it.col in col - 1..col + 1 }
    fun ElfPos.hasWestAny() = posSet.any { it.row in row - 1..row + 1 && it.col == col - 1 }
    fun ElfPos.hasEastAny() = posSet.any { it.row in row - 1..row + 1 && it.col == col + 1 }

    fun ElfPos.isNotAlone() = hasNorthAny() || hasSouthAny() || hasEastAny() || hasWestAny()
}