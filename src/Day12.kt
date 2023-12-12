// https://adventofcode.com/2023/day/12
fun main() {
    val today = "Day12"

    val input = readInput(today)
    val testInput = readTestInput(today)

    val theRegex = "([^.]+)".toRegex()

    val cache = mutableMapOf<LineAndPattern, Long>()
    fun parse1(input: List<String>): List<LineAndPattern> = input.map { it ->
        it.split(" ").let {
            LineAndPattern(it[0].trim('.'), it[1].split(",").map { it.toInt() })
        }
    }

    fun parse2(input: List<String>): List<LineAndPattern> = input.map { it ->
        it.split(" ").let {
            LineAndPattern("${it[0]}?".repeat(5).dropLast(1),
              "${it[1]},".repeat(5).trimEnd(',').split(",").map { it.toInt() })
        }
    }


    fun findArrangements(key: LineAndPattern): Long {
        return cache[key] ?: run {
            val (line, pattern) = key

            if (pattern.isEmpty()) return@run if (line.none { it == '#' }) 1 else 0
            val requiredSize = pattern.first()
            val requiredPattern = "#".repeat(requiredSize)
            val matchGroup = theRegex.find(line)?.groups?.get(0) ?: return@run 0
            val firstMatch = matchGroup.value
            if (firstMatch == requiredPattern) {
                val newLine = line.replaceFirst(firstMatch, "")
                return@run findArrangements(LineAndPattern(newLine, pattern.drop(1)))
            } else if (firstMatch.length < requiredSize && '#' in firstMatch) {
                return@run 0
            } else if (firstMatch.length > requiredSize && firstMatch.startsWith("#$requiredPattern")) {
                return@run 0
            }
            return@run findArrangements(
              LineAndPattern(line.replaceFirst("?", "."), pattern)
            ) + findArrangements(
              LineAndPattern(line.replaceFirst("?", "#"), pattern)
            )
        }.also { cache[key]=it }

    }

    fun part1(input: List<String>): Long {
        return parse1(input).sumOf { findArrangements(it) }
    }

    fun part2(input: List<String>): Long {
        return parse2(input).sumOf { findArrangements(it) }
    }

    chkTestInput(part1(testInput), 21L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 525152L, Part2)
    println("[Part2]: ${part2(input)}")
}

data class LineAndPattern(val line: String, val pattern: List<Int>)