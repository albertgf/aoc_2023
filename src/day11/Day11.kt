import kotlin.math.abs

object Day11 {
    fun <T>List<List<T>>.transpose(): List<List<T>> {
        return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }
    }
    fun spaceMap(input: List<String>, wrapSpace: Long) : SpaceMap {
        val listRows = mutableListOf<Int>()
        input.forEachIndexed { index, line -> if (line.contains('#').not()) listRows.add(index) }
        val transposed = input.map { it.map { it } }.transpose()
        val listColumns = mutableListOf<Int>()
        transposed.mapIndexed { x, line -> if (line.contains('#').not()) listColumns.add(x)}
        
        return SpaceMap(input, listRows, listColumns, wrapSpace)
    }
    
    fun searchGalaxies(input: List<String>) : List<GalaxyPoint> {
        var id = 0
        return input.flatMapIndexed { y, s -> s.mapIndexed { x, c ->
            GalaxyPoint(x, y, if (c == '#') id++ else null)
        } }
    }
    
    fun findUniquePairs(input: List<GalaxyPoint>): Set<Pair<GalaxyPoint, GalaxyPoint>> {
        val map = HashMap<Pair<GalaxyPoint, GalaxyPoint>, Boolean>()
        input.forEach { first ->
            input.forEach { second ->
                if (first != second) {
                    val (small, big) = listOf(first, second).sortedBy { it.id }
                    map[Pair(small, big)] = true
                }
            }
        }
        return map.keys.toSet()
    }
    fun distance(start: GalaxyPoint, end: GalaxyPoint, horizontalWrapZone: List<Int>, verticalWrapZone: List<Int>, wrapSpace: Long) : Long {
        val rangeX = if (start.x < end.x )start.x..end.x else end.x..start.x
        val wrapX = rangeX.count { it in verticalWrapZone }
        val rangeY = if (start.y < end.y) start.y..end.y else end.y..start.y
        val wrapY = rangeY.count { it in horizontalWrapZone }
        return abs(start.y - end.y) + abs(start.x - end.x) + (wrapX*wrapSpace) + (wrapY*wrapSpace)
    }

    fun part1(input: List<String>, wrapSpace: Long = 1) : Long  {
        val space = spaceMap(input, wrapSpace)
        val knowGalaxies = searchGalaxies(space.input).filterNot { it.id == null }
        val combinations = findUniquePairs(knowGalaxies)
        return combinations.sumOf { distance(it.first, it.second, space.horizontalWrapZone, space.verticalWrapZone, space.wrapSpace) }
    }

    fun part2(input: List<String>, wrapSpace: Long) = part1(input, wrapSpace)
}
data class SpaceMap(
    val input: List<String>,
    val horizontalWrapZone: List<Int>,
    val verticalWrapZone: List<Int>,
    val wrapSpace: Long
)

data class GalaxyPoint(
    val x: Int,
    val y: Int,
    val id: Int? = null
)

fun main() {
    val testInput = readInput("Day11/Day11_test")
    val input = readInput("Day11/Day11")
    val galaxies = Day11.searchGalaxies(testInput).filterNot { it.id == null }
    check(galaxies.size == 9)
    check(Day11.findUniquePairs(galaxies).size == 36)
    check(Day11.part1(testInput) == 374L)
    check(Day11.part2(testInput, 9L) == 1030L)
    check(Day11.part2(testInput, 99L) == 8410L)
    
    timetrack {
        println(Day11.part1(input,))
    }
    
    timetrack {
        println(Day11.part2(input, 999999))
    }
}