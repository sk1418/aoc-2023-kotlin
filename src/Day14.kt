// https://adventofcode.com/2023/day/14
fun main() {
    val today = "Day14"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun toCharMatrix(lines: List<String>) = lines.map { it.toList() }

    // [ [a,b],[c,d],[x,y] ] -> [a, b,#, c, d, #, x, y]
    fun List<List<Char>>.joinByHash() = map { it.joinChars() }.joinToString(separator = "#") { it }.toList()

    //  [[a, b,#, c, d, #, x, y],[..] ] -> [ [ [a,b],[c,d],[x,y] ], [...] ]
    fun List<List<Char>>.splitByHash() = map { charList -> charList.joinChars().split('#').map { it.toList() } }

    // 'O' > '.'
    fun List<List<List<Char>>>.slide(desc: Boolean = false) = map { charList -> charList.map { it.sorted().let { if (desc) it.reversed() else it } }.joinByHash() }

    fun List<List<Char>>.slideSouth() = transpose().splitByHash().slide().transpose()
    fun List<List<Char>>.slideNorth() = transpose().splitByHash().slide(true).transpose()
    fun List<List<Char>>.slideEast() = splitByHash().slide()
    fun List<List<Char>>.slideWest() = splitByHash().slide(true)
    fun List<List<Char>>.calcLoad() = reversed().mapIndexed { i, chars -> chars.count { it == 'O' } * (i + 1) }.sum()

    fun part1(input: List<String>): Int {
        return toCharMatrix(input).slideNorth().calcLoad()
    }

    fun part2(input: List<String>): Int {
        var result = toCharMatrix(input)
        repeat(1000) { result = result.slideNorth().slideWest().slideSouth().slideEast() }
        return result.calcLoad()
    }

    chkTestInput(part1(testInput), 136, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 64, Part2)
    println("[Part2]: ${part2(input)}")
}