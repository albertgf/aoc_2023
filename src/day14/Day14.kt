import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.pow

object Day14 {
    fun arrangeCircularRocksRight(input : String) : String {
        val ca = input.toCharArray()
        for (i in ca.indices) {
            val first = ca[i]
            val second = ca[(i+1).coerceAtMost(ca.lastIndex)]
            if (first == '.' && second == 'O')  {
                ca[i] = second
                ca[(i+1).coerceAtMost(ca.lastIndex)] = first
            }
        }
        
        val line = ca.joinToString("")
                
        if (line != input) return arrangeCircularRocksRight(line)
        return input
    }
    fun arrangeCircularRocksLeft(input : String) : String {
        val ca = input.toCharArray()
        for (i in ca.indices) {
            val first = ca[ca.lastIndex - i]
            val second = ca[(ca.lastIndex - i - 1).coerceAtLeast(0)]
            if (first == '.' && second == 'O')  {
                ca[ca.lastIndex - i] = second
                ca[(ca.lastIndex - i - 1).coerceAtLeast(0)] = first
            }
        }

        val line = ca.joinToString("")

        if (line != input) return arrangeCircularRocksLeft(line)
        return input
    }
    
    fun cycle(input: List<String>,) : List<String> {
        val north = input.transpose().map { arrangeCircularRocksRight(it) }
        val west = north.transpose().map { arrangeCircularRocksRight(it) }
        val south = west.transpose().map { arrangeCircularRocksLeft(it) }
        return south.transpose().map { arrangeCircularRocksLeft(it) }
    }
    
    fun part1(input: List<String>) : Int  {
        val map = input.transpose()
            .map {
                val arranged = arrangeCircularRocksRight(it)
                arranged
            }
         val transposed =  map.transpose()
             val sum = transposed.mapIndexed { index, s -> (input.size - index) * s.count { it == 'O' }  }.sum()
        return sum
    }
    
    fun part2(input: List<String>) : Int {
        val map = hashMapOf<String,String>()
        val list = mutableListOf<String>()
        var reflector = input
        var i = 0
        var startIndex = Int.MAX_VALUE
        run repeatBlock@ {
            repeat(1000000000) {
                if (map[reflector.toString()] == null) {
                    val temp = cycle(reflector)
                    map[reflector.toString()] = temp.toString()
                    reflector = temp
                } else if (list.contains(reflector.toString())){
                    return@repeatBlock;
                } else {
                    startIndex = minOf(i, startIndex)
                    list.add(reflector.toString())
                    reflector = map[reflector.toString()]?.drop(1)?.dropLast(1)?.split(", ") ?: emptyList()
                }
                ++i
            }
        }
        val start = (1000000000 - startIndex) % list.size
        return list[start].drop(1).dropLast(1).split(", ")
            .mapIndexed {  index, s -> (input.size - index) * s.count { it == 'O' }  }.sum()
    }
}

fun main() {
    val testInput = readInput("Day14/Day14_test")
    val input = readInput("Day14/Day14")
   
    check(Day14.part1(testInput) == 136)
    check(Day14.part2(testInput) == 64)
    
    timetrack {
        println(Day14.part1(input))
    }
    
    timetrack {
        println(Day14.part2(input))
    }
}