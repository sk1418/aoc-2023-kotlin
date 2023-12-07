// https://adventofcode.com/2023/day/7
fun main() {
    val today = "Day07"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun toHands(input: List<String>, withJoker: Boolean = false): List<Hand> {
        return input.map { line ->
            val (str, bid) = line.split(" ").let { it[0] to it[1].toInt() }
            val cards = str.asSequence().map { card ->
                when (card) {
                    'T' -> 10
                    'A' -> 14
                    'K' -> 13
                    'Q' -> 12
                    'J' -> if (withJoker) 1 else 11
                    else -> card.digitToInt()
                }
            }.toList()
            Hand(cards, bid, withJoker)
        }
    }

    fun part1(input: List<String>): Long {
        return toHands(input).sorted().foldIndexed(0) { idx, acc, hand -> acc + hand.bid * (idx + 1) }
    }

    fun part2(input: List<String>): Long {
        return toHands(input, withJoker = true).sorted().foldIndexed(0) { idx, acc, hand -> acc + hand.bid * (idx + 1) }
    }

    chkTestInput(part1(testInput), 6440L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 5905L, Part2)
    println("[Part2]: ${part2(input)}")
}

data class Hand(val cards: List<Int>, val bid: Int, val withJoker: Boolean = false) : Comparable<Hand> {
    private val joker = 1
    private val jokerCount = cards.count { it == joker }
    private val cardGroups = cards.groupingBy { it }.eachCount().toMutableMap().apply {
        if (withJoker && jokerCount in 1..4) { //excluding the all-joker case
            remove(joker)
            val maxCnt = maxOf { it.value }
            val key = keys.first { this[it] == maxCnt }
            this[key] = this[key]!! + jokerCount
        }
    }

    private val typeComparator = compareByDescending<Hand> { hand -> if (hand.withJoker) 0 else hand.cards.distinct().size }
        .thenByDescending { hand -> hand.cardGroups.size }
        .thenBy { hand -> hand.cardGroups.maxOf { it.value } }

    //both should have the same withJoker option (check omitted)
    override fun compareTo(other: Hand): Int {
        val typeResult = typeComparator.compare(this, other)
        return if (typeResult != 0) typeResult else cards.mapIndexed { i, v -> v.compareTo(other.cards[i]) }.firstOrNull { it != 0 } ?: 0
    }
}