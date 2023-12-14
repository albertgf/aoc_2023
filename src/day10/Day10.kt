import java.io.File
import kotlin.concurrent.timerTask

fun Pair<Int, Int>.add(other: Pair<Int, Int>): Pair<Int, Int> =
    Pair(this.first + other.first, this.second + other.second)

object Day10 {
    private const val STARTING_CHAR = 'S'
    private const val NOT_LOOP = '.'

    private val inputs = readInput("Day10/Day10")

    private val pipes = mapOf(
        '|' to listOf(Pair(0, 1), Pair(0, -1)),
        'L' to listOf(Pair(1, 0), Pair(0, -1)),
        'J' to listOf(Pair(-1, 0), Pair(0, -1)),
        '7' to listOf(Pair(0, 1), Pair(-1, 0)),
        'F' to listOf(Pair(0, 1), Pair(1, 0)),
        '-' to listOf(Pair(-1, 0), Pair(1, 0)),
        '.' to emptyList(),
        STARTING_CHAR to listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
    )

    private val verticalPipes = setOf('|', 'L', 'J', '7', 'F','S')

    private fun getCharAt(position: Pair<Int, Int>): Char =
        inputs[position.second][position.first]

    private fun getStartPosition(): Pair<Int, Int> {
        for (y in inputs.indices) {
            val x = inputs[y].indexOf(STARTING_CHAR)
            if (x != -1) return Pair(x, y)
        }
        throw NoSuchElementException("Start position not found")
    }

    private fun getFirstPipe(startPosition: Pair<Int, Int>): Pair<Int, Int> {
        pipes[STARTING_CHAR]?.forEach { direction ->
            val position = startPosition.add(direction)
            if (pipes[getCharAt(position)]!!.any { getCharAt(position.add(it)) == STARTING_CHAR })
                return position
        }
        throw NoSuchElementException("First pipe not found")
    }

    private fun getLoop(): List<Pair<Int, Int>> {
        var last = getStartPosition()
        var current = getFirstPipe(last)
        val list = mutableListOf<Pair<Int, Int>>()
        list.add(current)
        while (getCharAt(current) != STARTING_CHAR) {
            val newCurrent = current.add(pipes[getCharAt(current)]!!.first { current.add(it) != last })
            last = current
            current = newCurrent
            list.add(current)
        }

        return list
    }

    private fun getEnclosedPositions() : Int {
        val loop = getLoop()
        val filteredInputs = inputs.mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                if (loop.contains(Pair(x, y))) c else '.'
            }.joinToString("")
        }
        var raycast = 0
        val raycastInputs = filteredInputs.mapIndexed { y, s ->
            raycast = 0
            s.mapIndexed { x, c ->
                if (c in verticalPipes) ++raycast
                if (c == '.') (raycast % 2).toString() else c
            }.joinToString("")
        }
        return raycastInputs.toString().count { it == '1' }
    }
    

    

    fun part1() = println((getLoop().size + 1) / 2)

    fun part2() = println(getEnclosedPositions())
}

fun main() {
    timetrack {
        Day10.part1()
    }
    
    timetrack {
        Day10.part2()
    }
}