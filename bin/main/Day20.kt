import kotlin.math.abs

// https://adventofcode.com/2023/day/20
fun main() {
    val today = "Day20"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        val oList = input.map { it.toLong() }.toMutableList()
        val nList = initNodes(oList, 1L)
        oList.forEachIndexed { i, _ -> nList.first { it.oIdx == i }.move() }
        val zNode = nList.first { it.value == 0L }
        return xthAfter(zNode, 1000) + xthAfter(zNode, 2000) + xthAfter(zNode, 3000)
    }

    fun part2(input: List<String>): Long {
        val oList = input.map { it.toLong() }.toMutableList()
        val nList = initNodes(oList, 811589153L)
        repeat(10) { oList.forEachIndexed { i, _ -> nList.first { it.oIdx == i }.move() } }
        val zNode = nList.first { it.value == 0L }
        return xthAfter(zNode, 1000) + xthAfter(zNode, 2000) + xthAfter(zNode, 3000)
    }

    chkTestInput(part1(testInput), 3L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 1623178306L, Part2)
    println("[Part2]: ${part2(input)}")
}

fun initNodes(oList: List<Long>, key: Long): List<Node> {
    val nList = oList.mapIndexed { idx, v -> Node(idx, v * key, oList.size) }
    for (i in 0..oList.lastIndex) {
        val nextIdx = if (i == oList.lastIndex) 0 else i + 1
        nList[i].next = nList[nextIdx]
        val preIdx = if (i - 1 < 0) oList.lastIndex else i - 1
        nList[i].pre = nList[preIdx]
    }
    return nList
}

fun xthAfter(node: Node, x: Int): Long {
    var n = node
    repeat(x % node.total) { n = n.next!! }
    return n.value
}

data class Node(val oIdx: Int, val value: Long, val total: Int, var next: Node? = null, var pre: Node? = null) {
    fun move() {
        var n = this
        if (value % (total - 1) != 0L) {
            when {
                value > 0 -> {
                    repeat((value % (total - 1)).toInt()) { n = n.next!! }
                    this.pre!!.next = this.next
                    this.next!!.pre = this.pre
                    this.next = n.next
                    this.pre = n
                    n.next!!.pre = this
                    n.next = this
                }
                value < 0 -> {
                    repeat(abs(value % (total - 1)).toInt()) { n = n.pre!! }
                    this.pre!!.next = this.next
                    this.next!!.pre = this.pre
                    this.next = n
                    this.pre = n.pre
                    n.pre!!.next = this
                    n.pre = this
                }
            }
        }
    }
}
