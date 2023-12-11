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
        return checkNotNull( map[key]){"Key ($key) not found in the NeverNullMap"}
    }
}
class NotNullMap<K, V>(private val map: Map<K, V>) : Map<K, V> by map {
    override operator fun get(key: K): V {
        return checkNotNull( map[key]){"Key ($key) not found in the NeverNullMap"}
    }
}

open class Matrix<T:Any>(val maxX:Int, val maxY:Int, points:Map<Pair<Int,Int>, T>)