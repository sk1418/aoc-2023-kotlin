import java.util.*

// https://adventofcode.com/2023/day/17
fun main() {
    val today = "Day17"

    val input = readInput(today)
    val testInput = readTestInput(today)
    fun State.move(direction: Direction): State {
        val y = when (direction) {
            Direction.Up -> y - 1
            Direction.Down -> y + 1
            else -> y
        }
        val x = when (direction) {
            Direction.Left -> x - 1
            Direction.Right -> x + 1
            else -> x
        }
        return State(
            y = y,
            x = x,
            direction = direction,
            distance = if (direction == this.direction) distance + 1 else 1,
        )
    }

    fun bfs(
        input: List<String>,
        ok: (distance: Int) -> Boolean,
        next: (direction: Direction, distance: Int) -> Iterable<Direction>,
    ): Int? {
        val start = State(0, 0, Direction.Right, 0)
        val costs = mutableMapOf(start to 0)
        val queue = PriorityQueue<IndexedValue<State>>(compareBy { (cost, state) -> cost - state.y - state.x })
        queue.add(IndexedValue(0, start))
        while (!queue.isEmpty()) {
            val (cost, state) = queue.remove()
            if (state.y == input.lastIndex && state.x == input.last().lastIndex && ok(state.distance)) return cost
            if (costs.getValue(state) < cost) continue
            @Suppress("LoopWithTooManyJumpStatements")
            for (direction in next(state.direction, state.distance)) {
                val newState = state.move(direction)
                if (newState.y !in input.indices || newState.x !in input[state.y].indices) continue
                val newCost = cost + input[newState.y][newState.x].digitToInt()
                if (costs.getOrElse(newState) { Int.MAX_VALUE } <= newCost) continue
                costs[newState] = newCost
                queue.add(IndexedValue(newCost, newState))
            }
        }
        return null
    }

    fun part1(input: List<String>): Int {
        return bfs(input,
            ok = { true },
            next = { direction, distance ->
                if (distance < 3) listOf(direction.turn90(), direction.turn90Back(), direction) else listOf(direction.turn90(), direction.turn90Back())
            })!!
    }

    fun part2(input: List<String>): Int {
        return bfs(
            input,
            ok = { it >= 4 },
            next = { direction, distance ->
                when {
                    distance < 4 -> listOf(direction)
                    distance < 10 -> listOf(direction.turn90Back(), direction.turn90(), direction)
                    else -> listOf(direction.turn90(), direction.turn90Back())
                }
            },
        )!!
    }


    chkTestInput(part1(testInput), 102, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 94, Part2)
    println("[Part2]: ${part2(input)}")
}


private data class State(
    val y: Int,
    val x: Int,
    val direction: Direction,
    val distance: Int,
)