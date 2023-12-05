import java.util.LongSummaryStatistics
import kotlin.math.min
import kotlin.math.pow
import kotlin.time.measureTime

fun main() {
    data class Codex(val destinationStart: Long, val sourceStart: Long, val range: Long) {
        private val sourceRange: LongRange = sourceStart..(sourceStart+range)
        private val destinationRange: LongRange = destinationStart..(destinationStart+range)

        fun find(source: Long): Long? = if (source in sourceRange) destinationStart + (source - sourceStart) else null

        fun reverseFind(destination: Long): Long? =  if (destination in destinationRange) sourceStart + (destination - destinationStart) else null
    }

    data class Step(val entries: List<Codex>) {
        fun find(source: Long): Long =
            entries.firstNotNullOfOrNull { it.find(source) } ?: source
        fun findReverse(destination: Long) =
            entries.firstNotNullOfOrNull { it.reverseFind(destination) } ?: destination
    }

    fun steps(input: List<String>)   = input.drop(2).joinToString("\n").split("\n\n").map { section ->
        val list = section.lines().drop(1).map { line->
            line.split(" ").map { it.toLong()}.let { (dest, source, range) ->
                Codex(dest, source, range)
            }
        }
        Step(list)
    }

    fun seedsPart1(input: List<String>) = input.first().split(": ")[1].split(" ").map { it.toLong() }
    fun part1(input: List<String>): Long {
        val seeds = seedsPart1(input)

        val steps = steps(input)
        return  seeds.minOf { seed ->
            steps.fold(seed) { acc, step ->
                step.find(acc)
            }
        }
    }



    fun part2(input: List<String>): Long {
        val seeds = input.first().substringAfter(" ").split(" ").map { it.toLong() }.chunked(2).map { it.first()..<it.first() + it.last() }
        val steps = steps(input)

        val stepsReversed = steps.reversed()

        return generateSequence(0L) { it + 1 }.filter { location ->
            val seed = stepsReversed.fold(location){ acc, step -> step.findReverse(acc) }
            seeds.any { seed in it }
        }.first()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")

    val timeTaken = measureTime {
        part1(input).println()
    }
    println(timeTaken)
    val timeTaken2 = measureTime {
        part2(input).println()
    }
    println(timeTaken2)
    

}