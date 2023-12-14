import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.pow

object Day12 {
    const val SPRING_DAMAGED = '.'
    const val SPRING_WORKS = '#'
    const val SPRING_UNKNOWN = '?'
    
    data class SpringRecord(
        val input: String,
        val groups: List<Int>
    ) {
        fun getUnknownPositions() : List<Int> = input.withIndex().filter { it.value == SPRING_UNKNOWN }.map { it.index }

        fun getCombinations() : List<String> {
            val list = List (getUnknownPositions().size) { listOf(0,1)}
            val product = nCartesianProduct(list)
            val unknownPositions = getUnknownPositions()
            
            return product.map { list ->
                val builder = StringBuilder(input)
                list.forEachIndexed { index, any ->
                    builder.setCharAt(unknownPositions[index],if (any == 0) SPRING_DAMAGED else SPRING_WORKS)
                }
                builder.toString()
            }
        }
    }
    
    fun getSpringRecords(input: List<String>) = input.map { line -> 
        val (records, groups) = line.split(" ")
        SpringRecord(records, groups.split(",").map { it.toInt() })
    }
    
    fun getSpringRecordsUnfolded(input: List<String>) = input.map { line -> 
        val (records, groups) = line.split(" ")
        SpringRecord("$records?".repeat(5).dropLast(1), List (5) { groups.split(",").map { it.toInt() }}.flatten() )
    }
    
    fun removeDamagedRepetitions(input: String) =
        Regex("([.])\\1{1}").replace(input, SPRING_DAMAGED.toString())
    
    
    fun part1(input: List<String>) : Int  {
        val records = getSpringRecords(input)
        val count = records.map { record ->
            record.getCombinations().filter { combination ->
                val posible = removeDamagedRepetitions(combination).split(SPRING_DAMAGED).filter { it.isNotEmpty() }.map { it.length }
                posible == record.groups
            }.size
        }
        
        return count.sum()
    }
    
    private val cache = hashMapOf<Pair<String, List<Int>>, Long>()
    private fun count(config: String, groups: List<Int>): Long {
        if (groups.isEmpty()) return if (SPRING_WORKS in config) 0 else 1
        if (config.isEmpty()) return 0

        return cache.getOrPut(config to groups) {
            var result = 0L
            if (config.first() in ("$SPRING_DAMAGED$SPRING_UNKNOWN"))
                result += count(config.drop(1), groups)
            if (config.first() in "#?" && groups.first() <= config.length && "." !in config.take(groups.first()) && (groups.first() == config.length || config[groups.first()] != '#'))
                result += count(config.drop(groups.first() + 1), groups.drop(1))
            result
        }
    }

    fun part2(input: List<String>) : Long {
        val records = getSpringRecordsUnfolded(input)
        return records.sumOf { record ->
            count(record.input, record.groups)
        }
    }
}

fun main() {
    val testInput1 = readInput("Day12/Day12_test_1")
    val testInput2 = readInput("Day12/Day12_test_2")
    val input = readInput("Day12/Day12")
    
    val testRecords = Day12.getSpringRecords(testInput1)
    val combinations = testRecords.first().getCombinations()
    check(testRecords.size == 6)
    check(testRecords.first().getUnknownPositions().size == 3)
    
    check(Day12.part1(testInput1) == 21)
    check(Day12.part2(testInput1) == 525152L)
    
    timetrack {
        //println(Day12.part1(input,))
    }
    
    timetrack {
        println(Day12.part2(input))
    }
}