import java.util.regex.Pattern

fun main() {
    fun getMap(input: List<String>): Map<String, Pair<String, String>> {
        val map = HashMap<String, Pair<String, String>>().apply { 
            input.forEach {
                val (key, coordinates) = it.split(" = ")
                val (right, left) = coordinates.drop(1).dropLast(1).split(", ")
                this[key] = Pair(right,left)
            }
        }
        return map
    }
    
    fun Map<String, Pair<String, String>>.findEndRoute(instructions: String, startPoint: String, endCondition: (String) -> Boolean) : Int {
        var location = startPoint
        var count = 0
        do {
            var (left, right) = this[location]!!
            location = if (instructions[count % instructions.length] == 'L') left else right
            count++
        } while (endCondition(location).not())
        return count
    }
    
    fun part1(input: List<String>): Int {
        val map = getMap(input.drop(2))
        var startPoint = "AAA"
        val instructions = input.first()
        
        return map.findEndRoute(instructions, startPoint, ) { location -> location == "ZZZ"}
    }

    fun part2(input: List<String>): String {
        val instructions = input.first()
        val map = getMap(input.drop(2))
        return map.keys.filter { location -> location.last() == 'A' }.map { start ->
            map.findEndRoute(instructions, start) { location -> location.endsWith('Z') }.toBigInteger()
        }.reduce { acc, bigInteger -> acc * bigInteger / acc.gcd(bigInteger)}.toString()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08/Day08_test")
    check(getMap(testInput.drop(2)).size == 7)
    
    check(part1(testInput) == 2)
    val testInputb = readInput("day08/Day08_testb")
    check(part1(testInputb) == 6)
    val testInputC = readInput("day08/Day08_testc")
    check(part2(testInputC) == "6")

    val input = readInput("day08/Day08")

    timetrack {
        part1(input).println()
    }
    //19951

    timetrack {
        part2(input).println()
    }
    //16342438708751    
}