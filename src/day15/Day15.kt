import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.pow

object Day15 {
    fun hashAlgorithm(input: String) : Int {
        var value = 0
        input.forEach { c ->
            value = (value + c.code)*17%256
        }
        return value
    }
    
    fun getValue(input: String) : Value {
        return if (input.last() == '-') Value(OPERATION.REMOVE, hashAlgorithm(input.dropLast(1)), input.dropLast(1), )
        else Value(OPERATION.ADD_OR_REPLACE, hashAlgorithm(input.dropLast(2)), input.dropLast(2), input.last().toString().toInt())
    }
    
    enum class OPERATION {
        ADD_OR_REPLACE, REMOVE
    }
    
    data class Value(
        val operation: OPERATION,
        val box: Int,
        val label: String,
        val lens: Int? = null
            )
    
    fun part1(input: List<String>) : Int  {
        return input.joinToString("").split(",").sumOf { hashAlgorithm(it) }
    }
    
    fun part2(input: List<String>) : Int {
        val boxes = List(256) { LinkedHashMap<String, Int>() }
        input.joinToString("").split(",").map {
            val value = getValue(it)
            if (value.operation == OPERATION.REMOVE) {
                boxes[value.box].remove(value.label)
            } else {
                boxes[value.box][value.label] = value.lens!!
            }
        }
        return boxes.mapIndexed { boxIndex, box ->
            box.entries.mapIndexed { index, mutableEntry ->
                (boxIndex + 1) * (index + 1) *  mutableEntry.value }.sum()
        }.sum()
    }
}

fun main() {
    val testInput = readInput("Day15/Day15_test")
    val input = readInput("Day15/Day15")
    
    check(Day15.hashAlgorithm("HASH") == 52)
    check(Day15.hashAlgorithm("rn") == 0)
    
    check(Day15.part1(testInput) == 1320)
    check(Day15.part2(testInput) == 145)
    
    timetrack {
        println(Day15.part1(input))
    }
    
    timetrack {
        println(Day15.part2(input))
    }
}