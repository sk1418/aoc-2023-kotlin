import kotlin.math.min

// https://adventofcode.com/2023/day/13
fun main() {
    val today = "Day13"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun parse(input: List<String>): List<List<String>> {
        var list = mutableListOf<String>()
        val result = buildList<List<String>> {
            input.forEach {
                if (it.isBlank()) {
                    add(list)
                    list = mutableListOf()
                } else {
                    list += it
                }
            }
            add(list)
        }
        return result
    }

    fun findMirror(block: List<String>): Int {
        for (i in 1..<block.size) {
            val firstPart = block.subList(0, i).reversed()
            val secondPart = block.drop(i)
            val n = min(firstPart.size, secondPart.size)
            if (firstPart.subList(0, n) == secondPart.subList(0, n)) return i
        }
        return 0
    }

    fun theyAreSimilar(firstPart: List<String>, secondPart: List<String>): Boolean {
        val n = min(firstPart.size, secondPart.size)
        var result = false
        for (i in 0..<n) {
            val f = firstPart[i]
            val s = secondPart[i]

            val diff = (0..f.lastIndex).count { f[it] != s[it] }
            if (diff > 1) return false
            if (diff == 1) {
                if (result) return false else result = true //no other diff=1
            }
        }
        return result
    }

    fun findMirror2(block: List<String>): Int {
        for (i in 1..<block.size) {
            val firstPart = block.subList(0, i).reversed()
            val secondPart = block.drop(i)
            if (theyAreSimilar(firstPart, secondPart)) return i
        }
        return 0
    }


    fun transposeBlock(block: List<String>) =
        block.map { it.toList() }.transpose().map { it.joinToString(separator = "") { "$it" } }

    fun part1(input: List<String>): Int {
        val blocks = parse(input)
        return blocks.sumOf { b ->
            val h = findMirror(b) * 100
            if (h > 0) h else findMirror(transposeBlock(b))
        }
    }

    fun part2(input: List<String>): Int {
        val blocks = parse(input)
        return blocks.sumOf { b ->
            val h = findMirror2(b) * 100
            if (h > 0) h else findMirror2(transposeBlock(b))
        }
    }

    chkTestInput(part1(testInput), 405, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 400, Part2)
    println("[Part2]: ${part2(input)}")
}