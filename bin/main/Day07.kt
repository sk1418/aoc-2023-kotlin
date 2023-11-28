// https://adventofcode.com/2023/day/7
fun main() {
    val today = "Day07"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        sizeList.clear()
        buildTree(input).calcSize()
        return sizeList.filter { it <= 100000 }.sum()
    }

    fun part2(input: List<String>): Long {
        sizeList.clear()
        val root = buildTree(input)
        val needToDel = 30000000L - (70000000L - root.calcSize())
        return sizeList.sorted().first { it >= needToDel }
    }

    chkTestInput(part1(testInput), 95437L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 24933642L, Part2)
    println("[Part2]: ${part2(input)}")
}

fun buildTree(input: List<String>): Dir {
    val root = Dir("/", null)
    var curDir = root
    input.drop(1).forEach {
        when {
            it.endsWith("..") -> curDir = curDir.parent!!
            it.startsWith("$ cd ") -> curDir = it.substringAfterLast(" ").let { dirName -> curDir.getSubDir(dirName) }
            it.startsWith("dir ") -> curDir.subDirs.add(Dir(it.substringAfter(" "), curDir))
            it.matches(Regex("^\\d+ .*")) -> it.split(" ").also { fLine ->
                curDir.files.add(TheFile(fLine.last(), fLine.first().toLong()))
            }
        }
    }
    return root
}

var sizeList: MutableList<Long> = mutableListOf()

data class TheFile(val name: String, val size: Long)
data class Dir(
    val name: String, val parent: Dir?,
    val subDirs: MutableList<Dir> = mutableListOf(), val files: MutableList<TheFile> = mutableListOf()
) {
    fun getSubDir(name: String) = subDirs.first { it.name == name }
    fun calcSize(): Long = (files.sumOf { it.size } + subDirs.sumOf { it.calcSize() }).also { sizeList += it }
}