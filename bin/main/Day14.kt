import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2023/day/14
fun main() {
    val today = "Day14"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        initEverything(input)
        while (true) {
            if (sandDrop() !in posMap) break
        }
        return cnt
    }

    fun part2(input: List<String>): Int {
        initEverything(input)
        maxY += 2
        while (Pos14(500, 0) !in posMap) sandDrop2()
        return cnt
    }

    chkTestInput(part1(testInput), 24, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 93, Part2)
    println("[Part2]: ${part2(input)}")
}

var cnt = 0
val posMap = mutableSetOf<Pos14>()
var maxY = 0

fun initEverything(input: List<String>) {
    cnt = 0
    posMap.clear()
    input.forEach { line ->
        line.split(" -> ").windowed(2).forEach {
            addRockLine(
                it[0].split(",").let { xy -> Pos14(xy[0].toInt(), xy[1].toInt()) },
                it[1].split(",").let { xy -> Pos14(xy[0].toInt(), xy[1].toInt()) })
        }
    }
    maxY = posMap.maxOf { p -> p.y }
}

private fun addRockLine(pos1: Pos14, pos2: Pos14) {
    if (pos1.x == pos2.x) {
        (min(pos1.y, pos2.y)..max(pos1.y, pos2.y)).forEach {
            posMap += Pos14(pos1.x, it)
        }
    } else {
        (min(pos1.x, pos2.x)..max(pos1.x, pos2.x)).forEach {
            posMap += Pos14(it, pos1.y)
        }
    }
}

fun sandDrop(): Pos14 {
    var p = Pos14(500, 0)
    while (p.y <= maxY && p !in posMap) {
        p = p.drop()
    }
    return p
}

fun sandDrop2() {
    var p = Pos14(500, 0)
    while (p !in posMap) {
        p = p.drop2()
    }
}

data class Pos14(val x: Int, val y: Int) {

    fun drop2(): Pos14 {
        return drop().also {
            if (it !in posMap && it.y == maxY - 1) {
                posMap += it
                cnt++
            }
        }
    }

    fun drop(): Pos14 = listOf(Pos14(x, y + 1), Pos14(x - 1, y + 1), Pos14(x + 1, y + 1))
        .firstOrNull { it !in posMap } ?: this.also {
        cnt++
        posMap += it
    }
}

