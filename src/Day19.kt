// https://adventofcode.com/2023/day/19
fun main() {
    val today = "Day19"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun parseInput(input: List<String>): Pair<List<NotNullMap<String, Int>>, Checker> {
        val blankIdx = input.indexOf("")
        println(blankIdx)
        val (workflowLines, dataLines) = input.let { it.subList(0, blankIdx) to it.subList(blankIdx + 1, it.size) }

        //workflow
        val wfMap = mutableMapOf<String, List<String>>()
        workflowLines.forEach { line ->
            val (name, rules) = line.split("[{}]".toRegex())
            wfMap[name] = rules.split(',')
        }
        val workflows: NotNullMap<String, List<String>> = NotNullMap(wfMap)

        //data
        val data = dataLines.map { line ->
            NotNullMap(
                buildMap {
                    line.trim('{', '}').split(',').forEach { exp ->
                        exp.split('=').also { put(it[0], it[1].toInt()) }
                    }
                })
        }
        return data to Checker(workflows)

    }


    fun part1(input: List<String>): Int {
        val (data, checker) = parseInput(input)
        return data.sumOf { dataMap ->
            checker.check(dataMap, "in") * dataMap.values.sum()
        }
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    chkTestInput(part1(testInput), 19114, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 0L, Part2)
    println("[Part2]: ${part2(input)}")
}

data class Checker(val workflow: NotNullMap<String, List<String>>) {

    fun check(dataMap: NotNullMap<String, Int>, name: String): Int {
        val rules = workflow[name]
        rules.forEach { rule ->
            if (rule == "A") return 1
            if (rule == "R") return 0
            if (rule[1] in "<>") {
                val ret = valueCompare(dataMap, rule)
                if (ret.length > 1) return check(dataMap, ret)
                if (ret == "A") return 1
                if (ret == "R") return 0
                return@forEach
            }
            return check(dataMap, rule)
        }
        return 0
    }


    private fun valueCompare(dataMap: NotNullMap<String, Int>, rule: String): String {

        val (n, compV, result) = rule.split("[<>:]".toRegex())
        val op = rule[1]
        val chkResult = when (op) {
            '>' -> dataMap[n] > compV.toInt()
            '<' -> dataMap[n] < compV.toInt()
            else -> error("XXX")
        }
        return if (chkResult) result else ""
    }

}