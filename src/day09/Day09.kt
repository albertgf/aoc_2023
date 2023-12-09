import java.util.regex.Pattern

fun main() {
    fun addElement(line: List<Int>, calculate: (Int, Int) -> Int, zip: (Int, Int) -> Int) : Int {
        if (line.filterNot { it == 0 }.isNotEmpty() ) {
            val newline = line.zipWithNext { a, b -> zip(a, b) }
            val previous = addElement(newline, calculate, zip)
            return calculate(previous, line.last())
        }
        return 0
    }
    
    
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val inputs = line.split(" ").map { it.toInt() }
            addElement(inputs,
                    calculate = { a, b -> a + b },
                    zip = { a, b -> b - a })
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val inputs = line.split(" ").map { it.toInt() }.reversed()
            addElement(inputs,
                    calculate = { a, b -> b - a },
                    zip = { a, b -> a - b })
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput= readInput("day09/Day09_test")
    
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("day09/Day09")

    timetrack {
        part1(input).println()
    }
    //1930746032

    timetrack {
        part2(input).println()
    }
    //1154
}