import Direction.*
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

const val Part1 = "Part 1"
const val Part2 = "Part 2"

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src/inputs", "$name.txt").readLines()
fun readTestInput(name: String) = File("src/inputs", "$name-test.txt").readLines()

fun readInputAsInts(name: String) = File("src/inputs", "$name.txt").readLines().map { it.toInt() }

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun chkTestInput(actual: Number, expect: Number, part: String) {
    println("[TEST::$part]: $actual").also {
        check(actual == expect)
    }
}

fun chkTestInput(actual: String, expect: String, part: String) {
    println("[TEST::$part]: $actual").also {
        check(actual == expect)
    }
}

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val result = (first().indices).map { mutableListOf<T>() }.toMutableList()
    forEach { list -> result.zip(list).forEach { it.first.add(it.second) } }
    return result
}

class MutableNotNullMap<K, V>(private val map: MutableMap<K, V>) : MutableMap<K, V> by map {
    override operator fun get(key: K): V {
        return checkNotNull(map[key]) { "Key ($key) not found in the NeverNullMap" }
    }
}

class NotNullMap<K, V>(private val map: Map<K, V>) : Map<K, V> by map {
    override operator fun get(key: K): V {
        return checkNotNull(map[key]) { "Key ($key) not found in the NeverNullMap" }
    }
}

fun List<Char>.joinChars() = joinToString(separator = "") { "$it" }
open class Matrix<T : Any>(val maxX: Int, val maxY: Int, open val points: Map<Pair<Int, Int>, T>) {

    protected fun Pair<Int, Int>.move(direction: Direction) = when (direction) {
        Up -> first to (second - 1)
        Down -> first to (second + 1)
        Left -> (first - 1) to second
        Right -> (first + 1) to second
    }

    protected fun Pair<Int, Int>.safeMove(direction: Direction) = when (direction) {
        Up -> (first to ((second - 1).takeIf { it >= 0 } ?: 0))
        Down -> (first to ((second + 1).takeIf { it <= maxY } ?: maxY))
        Left -> (((first - 1).takeIf { it >= 0 } ?: 0) to second)
        Right -> (((first + 1).takeIf { it <= maxX } ?: maxX) to second)
    }

}

enum class Direction {
    Up, Down, Left, Right;

    fun turn90() = dirList.indexOf(this).let { if (it == 3) dirList.first() else dirList[it + 1] }
    fun turn90Back() = dirList.indexOf(this).let { if (it == 0) dirList.last() else dirList[it - 1] }

    companion object {
        private val dirList = listOf(Left, Up, Right, Down)
    }
}