import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.pow

object Day13 {
    private fun List<String>.transpose(): List<String> {
        return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }.map { it.joinToString() } }
    
    fun getPatterns(input: List<String>) : List<List<String>> = input.joinToString("\n").split("\n\n").map {
        it.split("\n") }
    
//    fun findReflex(input: List<CharArray>, original: Int) : Int {
//        val size = input.size
//        for(index in 0..size) {
//            if (index != original) {
//                val simetric = input.dropLast((size - ((index+1)*2)).coerceAtLeast(0)).drop((((index+1)*2)-size).coerceAtLeast(0)).map { it.toList() }
//                    .isSimetric()
//                if (simetric)  { return index + 1 }
//            }
//        }
//        return Int.MAX_VALUE
//    }
//    
//    fun List<List<Char>>.isSimetric() : Boolean {
//        if(this.size % 2 != 0 || this.size < 2) return false
//        val (first,second) = this.chunked(this.size/2)
//        return first == second.reversed()
//    }
//    fun findReflex2(input: List<CharArray>) : Int {
//        val valueVertical = findReflex(input, -1)
//        if (valueVertical > 0 && valueVertical != Int.MAX_VALUE) return valueVertical * 100
//        //val valueHorizontal = findReflex(input.transpose(), -1)
//        //if (valueHorizontal > 0 && valueHorizontal != Int.MAX_VALUE) return valueHorizontal
//        return 0
//    }
//    
//    fun findRefleReplace(input: List<CharArray>, maxRow: Int, original: Int) : Int {
//        val results = mutableListOf<Int>()
//        for (i in 0..<maxRow) {
//            for (x in 0..<input[i].size) {
//                val value = input[i][x]
//                input[i][x] = if (value == '.') '#' else '.'
//                val reflexed = findReflex(input, original)
//                if (reflexed > 0 && reflexed != Int.MAX_VALUE && reflexed != original) return reflexed
//                input[i][x] = value
//            }
//        }
//        return Int.MAX_VALUE
//    }
//    
//    fun findReflex3(input: List<CharArray>) : Int {
//        //val inputTransposed = input.transpose()
//        val valueVerticalOriginal = findReflex(input,-1)
//        //val valueHorizontalOriginal = findReflex(inputTransposed, -1)
//        val valueVertical = findRefleReplace(input, input.size, valueVerticalOriginal)
//        if (valueVertical > 0 && valueVertical != Int.MAX_VALUE) return valueVertical * 100
//        //val valueHorizontal = findRefleReplace(inputTransposed, inputTransposed.size, valueHorizontalOriginal)
//        //if (valueHorizontal > 0 && valueHorizontal != Int.MAX_VALUE) return valueHorizontal
//        
//        return 0
//    }
    
    fun getReflexion(input: List<String>, reflexionPoint: Int): Int {
        return (0..reflexionPoint).map {
            index -> reflexionPoint - index to reflexionPoint + index + 1
        }. filter { (a, last) ->  last <= input.size - 1}
        .sumOf { (first, second) -> input[first].difference(input[second]) }
    }
    
    fun horizontalReflection(input: List<String>, smudges: Int): Int? {
        return input.asSequence()
            .zipWithNext { a, b -> a.difference(b) }
            .mapIndexed { index, i -> index to i }
            .filter { it.second <= smudges }
            .map { it.first }
            .map { reflectionPoint -> getReflexion(input, reflectionPoint) to reflectionPoint}
            .firstOrNull { it.first == smudges }
            ?.second?.plus(1)
    }
    
    fun verticalReflection(input: List<String>, smudges: Int): Int? = horizontalReflection(input.transpose(), smudges)
    
    fun part1(input: List<String>) : Int  {
        val sum = getPatterns(input).sumOf {
            horizontalReflection(it, 0)?.times(100)
                    ?: verticalReflection(it, 0)
            ?: 0
        }
//        val sum = getPatterns(input).map { findReflex2(it) }
//       return getPatterns(input).sumOf { findReflex2(it) }
        return sum
    }
    
    fun part2(input: List<String>) : Int {
        val sum = getPatterns(input).sumOf {
            horizontalReflection(it, 1)?.times(100)
                    ?: verticalReflection(it, 1)
                    ?: 0
        }
//        val sum = getPatterns(input).map { findReflex3(it) }
//        return sum.sum()//getPatterns(input).sumOf { findReflex3(it) }
        return sum
    }
}

fun main() {
    val testInput = readInput("Day13/Day13_test")
    val testInput1 = readInput("Day13/Day13_test_1")
    val input = readInput("Day13/Day13")
    val dif = ".....#".difference("......")
    
    check(dif == 1)
    
    check(Day13.getPatterns(testInput).size == 2)
   
    check(Day13.part1(testInput) == 405)
    check(Day13.part2(testInput) == 400)
    //check(Day13.part2(testInput1) == 9)
    
    timetrack {
        println(Day13.part1(input))
    }
    
    timetrack {
        println(Day13.part2(input))
    }
//32497
}