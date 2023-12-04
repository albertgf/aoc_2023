import java.lang.Exception

fun main() {
    fun parser(input: List<String>, nums: ArrayList<NumberEngine>, filter: (Char) -> Boolean) {
        input.forEachIndexed { x, line ->
            var number = NumberEngine(0)
            line.forEachIndexed { y, c ->
                val surrounds = input.getSurrounds(x, y)

                val reset = number.addChar(c, surrounds, filter)
                if (!reset) {
                    nums.add(number)
                    number = NumberEngine(0)
                }
            }
            nums.add(number)
        }
    }

    fun part1(input: List<String>): Int {
        val nums = arrayListOf<NumberEngine>()
        parser(input, nums) { !it.isDigit() && it != '.' }

        return nums.filter { it.hasSimbol }.sumOf { it.value }
    }


    fun part2(input: List<String>): Int {
        val nums = arrayListOf<NumberEngine>()
        parser(input, nums) { it == '*' }
        val filtered = nums.filter { it.hasSimbol }
        var accumulator = 0
        filtered.forEachIndexed { index, numberEngine ->  
            val value = filtered.filterIndexed { indexCompared, compared ->
                compared.symbolPos == numberEngine.symbolPos && index < indexCompared }
                .firstOrNull()?.value ?: 0
            accumulator += numberEngine.value * value
        }
        return accumulator
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

fun List<String>.getSurrounds(x: Int, y: Int): List<Pos> {
    val surrounds = arrayListOf<Pos>()

    try {
        val char1 = this[x - 1][y - 1]
        surrounds.add(Pos(char1, x-1, y -1))
    } catch (e: Exception) {

    }
    try {
        val char2 = this[x-1][y]
        surrounds.add(Pos(char2,x-1,y))
    } catch (e: Exception) {

    }
    try {
        val char3 = this[x-1][y+1]
        surrounds.add(Pos(char3,x-1,y+1))
    } catch (e: Exception) {

    }
    try {
        val char4 = this[x][y-1]
        surrounds.add(Pos(char4,x,y-1))
    } catch (e: Exception) {

    }
    try {
        val char5 = this[x][y+1]
        surrounds.add(Pos(char5,x,y+1))
    } catch (e: Exception) {

    }
    try {
        val char6 = this[x+1][y-1]
        surrounds.add(Pos(char6,x+1,y-1))
    } catch (e: Exception) {

    }
    try {
        val char7 = this[x+1][y]
        surrounds.add(Pos(char7, x+1,y ))
    } catch (e: Exception) {

    }
    try {
        val char8 = this[x+1][y+1]
        surrounds.add(Pos(char8,x+1,y+1))
    } catch (e: Exception) {

    }

    return surrounds
}

data class Pos(
    val value: Char,
    val x: Int,
    val y: Int
)

data class NumberEngine(
        var value: Int,
        var hasSimbol: Boolean = false,
        var symbolPos: Pair<Int,Int>? = null
) {
    fun addChar(c: Char, surrounds: List<Pos>, filter: (Char) -> Boolean): Boolean {
        if (c.isDigit()) {
            this.value = "$value$c".toInt()
            val filtered = surrounds.filter { filter(it.value) }
            if (filtered.isNotEmpty()) {
                this.hasSimbol = true
                this.symbolPos = Pair(filtered.first().x, filtered.first().y)
            }
            return true
        } else {
            return false
        }
    }
}


