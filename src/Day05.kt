// https://adventofcode.com/2023/day/5
fun main() {
    val today = "Day05"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val reStartWithDigit = Regex("^\\d.*")

    fun parseInput(input: List<String>): SeedAndMaps {
        val seeds = input[0].split(": ")[1].split(" +".toRegex()).map { it.toLong() }
        val maps: MutableList<MutableList<Pair<LongRange, LongRange>>> = mutableListOf()
        var pairs: MutableList<Pair<LongRange, LongRange>> = mutableListOf()
        input.forEach { line ->
            if (line.isBlank()) {
                maps += pairs
                pairs = mutableListOf()
            }
            if (line.matches(reStartWithDigit)) {
                val mapLine = line.split(" ").map { it.toLong() }
                pairs += (mapLine[1]..<mapLine[1] + mapLine[2]) to (mapLine[0]..<mapLine[0] + mapLine[2])
            }
        }
        maps += pairs //add the last line
        return SeedAndMaps(seeds, maps).also { println("Input parsed") }
    }

    fun part1(input: List<String>): Long {
        val seedsAndMaps = parseInput(input)
        return seedsAndMaps.seeds.minOf { seed -> seedsAndMaps.findTheFinalStop(seed) }
    }

    fun part2(input: List<String>): Long {
        val seedsAndMaps = parseInput(input)
        return seedsAndMaps.rangeSeeds().minOf { range -> range.minOf { seed -> seedsAndMaps.findTheFinalStop(seed) } }
    }

    chkTestInput(part1(testInput), 35L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 46L, Part2)
    println("[Part2]: ${part2(input)}")
}

data class SeedAndMaps(val seeds: List<Long>, val maps: MutableList<MutableList<Pair<LongRange, LongRange>>>) {
    fun rangeSeeds() = seeds.chunked(2).map { it[0]..<it[0] + it[1] }
    fun findTheFinalStop(seed: Long): Long {
        return maps.fold(seed) { acc, pairs ->
            pairs.firstOrNull { acc in it.first }?.let {
                it.second.first + ((acc - it.first.first).toInt())
            } ?: acc
        }
    }
}