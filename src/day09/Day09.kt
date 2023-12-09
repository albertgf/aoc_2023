import java.util.regex.Pattern

fun main() {
    fun addElement(line: List<Int>) : Pair<MutableList<Int>,MutableList<Int>> {
        var listFront = mutableListOf<Int>()
        var listBackwards = mutableListOf<Int>()
        if (line.filterNot { it == 0 }.isNotEmpty() ) {
            val newline = line.zipWithNext { a, b -> b-a }
            val pair = addElement(newline)
            listFront = pair.first
            listBackwards = pair.second
        }
        val last = if (listFront.isNotEmpty()) listFront.last() else 0
        listFront.add(last + line.last())
        val first = if (listBackwards.isNotEmpty()) listBackwards.last() else 0
        listBackwards.add(line.first() - first)
        return Pair(listFront, listBackwards)
    }
    
    
    fun part1(input: List<String>): Int {
        val results = input.map { line ->
            val inputs = line.split(" ").map { it.toInt() }
            val (front, backwards) = addElement(inputs)
            front.last()
        }
        return results.sum()
    }

    fun part2(input: List<String>): Int {
        val results = input.map { line ->
            val inputs = line.split(" ").map { it.toInt() }
            val (front, backwards) = addElement(inputs)
            backwards.last()
        }
        return results.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput= readInput("day09/Day09_test")
    
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("day09/Day09")

    timetrack {
        part1(input).println()
    }
    //19951

    timetrack {
        part2(input).println()
    }
    //16342438708751    
}