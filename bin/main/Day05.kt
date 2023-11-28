// https://adventofcode.com/2023/day/5
fun main() {
    val today = "Day05"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>, numOfStacks: Int): String {
        init(input, numOfStacks)
        return part1AfterMove()
    }

    fun part2(input: List<String>, numOfStacks: Int): String {
        init(input, numOfStacks)
        return part2AfterMove()
    }

    chkTestInput(part1(testInput, 3), "CMZ", Part1)
    println("[Part1]: ${part1(input, 9)}")

    chkTestInput(part2(testInput, 3), "MCD", Part2)
    println("[Part2]: ${part2(input, 9)}")
}

val stacks: MutableList<ArrayDeque<String>> = mutableListOf()
var steps: List<MoveStep> = listOf()

data class MoveStep(val from: Int, val to: Int, val number: Int)

fun init(input: List<String>, numberOfStacks: Int) {
    val stepStartIdx = input.indexOfFirst { it.isBlank() } + 1
    initStacks(input.take(stepStartIdx - 1), numberOfStacks)
    initSteps(input.subList(stepStartIdx, input.size))
}

fun initStacks(stacksInput: List<String>, numberOfStacks: Int) {
    //init
    stacks.clear()
    for (i in 0 until numberOfStacks) {
        stacks.add(ArrayDeque())
    }
    //fill
    stacksInput.forEach {
        var stackIdx = 0
        for (i in 1 until it.length step 4) {
            if (it[i] in 'A'..'Z') {
                stacks[stackIdx].addFirst("${it[i]}")
            }
            stackIdx++
        }
    }
}

fun initSteps(stepLines: List<String>) {
    steps = stepLines.map {
        it.split(Regex("\\D+")).let { nums -> MoveStep(nums[2].toInt() - 1, nums.last().toInt() - 1, nums[1].toInt()) }
    }
}

fun part1AfterMove(): String {
    steps.forEach { step ->
        repeat(step.number) {
            stacks[step.to].add(stacks[step.from].removeLast())
        }
    }
    return stacks.joinToString(separator = "") { it.last() }
}

fun part2AfterMove(): String {
    steps.forEach { step ->
        stacks[step.to].addAll(stacks[step.from].takeLast(step.number))
        repeat(step.number) {
            stacks[step.from].removeLast()
        }
    }
    return stacks.joinToString(separator = "") { it.last() }
}
