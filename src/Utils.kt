import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTime

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun timetrack(lambda: () -> Unit) {
 val timeTaken = measureTime {
  lambda()
 }
 println(timeTaken)
}

fun flattenList(nestList: List<Any>): List<Any> {
 val flatList = mutableListOf<Any>()

 fun flatten(list: List<Any>) {
  for (e in list) {
   if (e !is List<*>)
    flatList.add(e)
   else
    @Suppress("UNCHECKED_CAST")
                flatten(e as List<Any>)
  }
 }

 flatten(nestList)
 return flatList
}

operator fun List<Any>.times(other: List<Any>): List<List<Any>> {
 val prod = mutableListOf<List<Any>>()
 for (e in this) {
  for (f in other) {
   prod.add(listOf(e, f))
  }
 }
 return prod
}


fun nCartesianProduct(lists: List<List<Int>>): List<List<Any>> {
 require(lists.size >= 2)
 return lists.drop(2).fold(lists[0] * lists[1]) { cp, ls -> cp * ls }.map { flattenList(it) }
}

fun String.difference(other: String) : Int {
 return this.mapIndexed { index, c -> c != other[index] }.count { it }
}

fun List<String>.transpose(): List<String> {
 return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }.map { it.joinToString("") } }
