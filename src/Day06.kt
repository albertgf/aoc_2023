import java.util.LongSummaryStatistics
import kotlin.concurrent.timerTask
import kotlin.math.min
import kotlin.math.pow
import kotlin.time.measureTime

fun main() {
    fun calculatePositiveOutcomes(time: Long, minDistance: Long): Long {
        var distance = 0L
        var speed = 0L
        while (minDistance >= distance) {
            speed++
            distance = (time - speed) * speed
        }
        return (time+1) - ((speed) * 2)
    }
    
    fun part1(input: List<String>): Long {
        val times = input.first().split(" ")
            .filterNot { it.isEmpty() }.drop(1).map { it.toLong() }
        val distances = input[1].split(" ")
            .filterNot { it.isEmpty() }.drop(1).map { it.toLong() }
        
        return times.mapIndexed { index, time ->
            calculatePositiveOutcomes(time, distances[index])
        }.reduce { acc, distance -> acc*distance }
    }

    fun part2(input: List<String>): Long {
        val time = input.first().split(" ")
            .filterNot { it.isEmpty() }.drop(1).reduce { acc, l -> acc+l }.toLong()
        val distance = input[1].split(" ")
            .filterNot { it.isEmpty() }.drop(1).reduce { acc, s -> acc+s }.toLong()

        return calculatePositiveOutcomes(time, distance)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")

    timetrack {
        part1(input).println()
    }

    timetrack {
        part2(input).println()
    }
}