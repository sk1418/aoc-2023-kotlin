// https://adventofcode.com/2023/day/15
fun main() {
    val today = "Day15"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun hash(str: String): Int {
        return str.fold(0) { acc, char -> (acc + char.code) * 17 % 256 }
    }

    fun part1(input: List<String>): Long {
        return input[0].split(',').sumOf {
            hash(it)
        }.toLong()
    }

    fun LinkedHashMap<String, Int>.boxValue() = entries.toList().foldIndexed(0) { i, acc, next -> (i + 1) * next.value + acc }

    fun part2(input: List<String>): Long {
        val boxes = List(256) { LinkedHashMap<String, Int>() }
        input[0].split(',').forEach { str ->
            val (label, value) = str.split('-', '=')
            val box = boxes[hash(label)]
            when {
                '=' in str -> box[label] = value.toInt()
                '-' in str -> box.remove(label)
            }
        }
        return boxes.foldIndexed(0) { i, acc, box ->
            (i + 1) * box.boxValue() + acc
        }

    }

    chkTestInput(part1(testInput), 1320L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 145L, Part2)
    println("[Part2]: ${part2(input)}")
}