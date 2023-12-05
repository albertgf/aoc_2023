import kotlin.math.min
import kotlin.math.pow

fun main() {
    fun buildSeeds(input: String) : List<Pair<Long, Long>> {
        val seeds = input.split(" ").zipWithNext { a, b ->
            Pair(a.toLong(), b.toLong())
        }
        
        return seeds.filterIndexed { index, pair -> index % 2 == 0 }
    }
    
    fun buildStep(input: String) : Step {
        val values = input.replace(" map", "").splitToPair("-to-")
        return Step(values.first, values.second)
    }
    
    fun destination(input: String, step: Step) {
        val values = input.split(" ")
        step.addDestinations(values[1].toLong(), values[0].toLong(), values[2].toLong())
    }
    

    fun part1(input: List<String>): Long {
        val seeds = input.first().split(": ")[1].split(" ").map { it.toLong() }
        val steps = arrayListOf<Step>()
        var step = Step("","")
        input.subList(2, input.size).forEach {
            val splitted = it.split(":")
            when (splitted.size) {
                1 -> if(it.isEmpty()) steps.add(step) else destination(it, step)
                2 -> step = buildStep(splitted.first())
            }
        }
        steps.add(step)
        val results = seeds.map { seed ->
            var value = seed
            steps.forEach {
                value = it.findDestination(value) }
            value
        }
        results
        return results.min()
    }


    fun part2(input: List<String>): Long {
        val seeds = buildSeeds(input.first().split(": ")[1])
        val steps = arrayListOf<Step>()
        var step = Step("","")
        input.subList(2, input.size).forEach {
            val splitted = it.split(":")
            when (splitted.size) {
                1 -> if(it.isEmpty()) steps.add(step) else destination(it, step)
                2 -> step = buildStep(splitted.first())
            }
        }
        steps.add(step)
        val results = seeds.map { seed ->
            var min = Long.MAX_VALUE
            for (i in 0..<seed.second) {
                var value = seed.first + i
                steps.forEach {
                    value = it.findDestination(value) }
                min = min(min, value)
            }
            min
        }
        return results.min()
    }
    val stepTest = Step("a","b", arrayListOf(Codex(98, 50 , 2)))
    check(stepTest.findDestination(98) == 50L)
    
    val inputStep = "seed-to-soil map"
    check(buildStep(inputStep).source == "seed")
    
    val inputSeed = "79 14 55 13"
    check(buildSeeds(inputSeed).size == 2,)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
    
    
}
data class Codex(
        val source: Long,
        val destination: Long,
        val range: Long
        )

data class Step(
    val source: String,
    val destination: String,
    val codex: ArrayList<Codex> = arrayListOf()
) {
    fun findDestination(value: Long) = codex.firstNotNullOfOrNull { codex ->
        if (value >= codex.source && value < codex.source + codex.range) {
            codex.destination + (value - codex.source)
        } else null
    } ?: value
    fun addDestinations(source: Long, destination: Long, range: Long) {
        codex.add(Codex(source, destination, range))
    }
}