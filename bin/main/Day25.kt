import kotlin.math.pow

// https://adventofcode.com/2023/day/25
fun main() {
    val today = "Day25"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): String {
        return sumSANFU(input).toSANFU()
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    chkTestInput(part1(testInput), "2=-1=0", Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 0L, Part2)
    println("[Part2]: ${part2(input)}")
}

fun sumSANFU(input: List<String>): Long {
    return input.sumOf { s ->
        var p = s.length - 1
        s.toList().sumOf { c ->
            5.toDouble().pow(p--.toDouble()) * when (c) {
                in '0'..'9' -> c.digitToInt()
                '-' -> -1
                '=' -> -2
                else -> throw IllegalStateException()
            }
        }.toLong()
    }
}

fun Long.toSANFU(): String {
    if(this == 0L) return ""
    val r = this % 5
    val s = "${"012=-"[r.toInt()]}"
    return "${(this / 5 + if (r < 3) 0 else 1).toSANFU()}$s"
}
