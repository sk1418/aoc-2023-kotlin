// https://adventofcode.com/2023/day/13
fun main() {
    val today = "Day13"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        val elementPairs = input.chunked(3).map { it[0].toElement() to it[1].toElement() }
        return elementPairs.mapIndexed { i, p -> if (p.first < p.second) i + 1 else 0 }.sum()
    }

    fun part2(input: List<String>): Int {
        val two = "[[2]]".toElement()
        val six = "[[6]]".toElement()
        return (listOf(two, six) + input.filter { it.isNotBlank() }.map { it.toElement() }).sorted().let { (1+it.indexOf(two)) * (1+it.indexOf(six)) }
    }

    chkTestInput(part1(testInput), 13, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 140, Part2)
    println("[Part2]: ${part2(input)}")
}

sealed interface Element : Comparable<Element>
data class IntElement(val value: Int) : Element {
    override fun compareTo(other: Element): Int = when (other) {
        is IntElement -> value.compareTo(other.value)
        is ListElement -> ListElement(mutableListOf(this)).compareTo(other)
    }
}

data class ListElement(val values: MutableList<Element> = mutableListOf()) : Element {
    override fun compareTo(other: Element): Int = when (other) {
        is IntElement -> compareTo(ListElement(mutableListOf(other)))
        is ListElement -> values.zip(other.values).asSequence().map { (a, b) -> a.compareTo(b) }.firstOrNull { it != 0 } ?: values.size.compareTo(other.values.size)
    }

    fun append(e: Element) {
        values.add(e)
    }
}

fun String.toElement(): Element {
    val listHistory = ArrayDeque<ListElement>()
    var curList = ListElement().also { listHistory.addFirst(it) }
    val numbers = mutableListOf<Char>()
    this.substring(1).toList().forEach {
        when (it) {
            '[' -> {
                val newList = ListElement()
                curList.append(newList)
                listHistory.addFirst(curList)
                curList = newList
            }
            ']' -> {
                if (numbers.isNotEmpty()) curList.append(IntElement(numbers.joinToString(separator = "") { "$it" }.toInt()))
                numbers.clear()
                curList = listHistory.removeFirst()
            }
            ',' -> {
                if (numbers.isNotEmpty()) curList.append(IntElement(numbers.joinToString(separator = "") { "$it" }.toInt()))
                numbers.clear()
            }
            else -> numbers.add(it)
        }
    }
    return curList
}
