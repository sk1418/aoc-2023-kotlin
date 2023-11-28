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
