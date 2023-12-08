// https://adventofcode.com/2023/day/8
fun main() {
    val today = "Day08"
    val part2 = "$today-part2"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val testPart2 = readTestInput(part2)

    val reSplit = "[^A-Z0-9]+".toRegex()

    fun buildNetwork(input: List<String>): NetworkDay8 = NetworkDay8(
        sequence { while (true) yieldAll(input[0].asSequence()) },
        input.drop(2).associate { line -> line.split(reSplit).let { it[0] to (it[1] to it[2]) } }
    )

    fun part1(input: List<String>): Int {
        return buildNetwork(input).walk()
    }

    fun part2(input: List<String>): Long {
        return buildNetwork(input).ghostWalk()
    }

    chkTestInput(part1(testInput), 6, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testPart2), 6L, Part2)
    println("[Part2]: ${part2(input)}")
}

private fun Pair<String, String>.follow(c: Char) = if (c == 'L') first else second
data class NetworkDay8(val instruction: Sequence<Char>, val routes: Map<String, Pair<String, String>>) {
    fun walk(start: String = "AAA", endWith: String = "ZZZ"): Int {
        var steps = 0
        val seq = instruction.iterator()
        var current = start
        while (!current.endsWith(endWith)) {
            current = routes[current]!!.follow(seq.next()).also { steps++ }
        }
        return steps
    }

    fun ghostWalk(): Long {
        val stepList = routes.keys.filter { it.endsWith("A") }.map { walk(it, "Z") }.toSet()
        return stepList.fold(1L) { acc, next -> lcm(acc, next.toLong()) }
    }

    private fun lcm(n1: Long, n2: Long) = n1 * n2 / gcd(n1, n2)
    private fun gcd(n1: Long, n2: Long): Long {
        var (a, b, c) = listOf(n1, n2, 0L)
        while (b > 0) {
            c = a
            a = b
            b = c % b
        }
        return a
    }
}