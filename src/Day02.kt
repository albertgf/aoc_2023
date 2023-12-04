fun main() {
    fun parseGame(line: String, gameRules: GameRules) : Game {
        val splitted = line.split(":")
        val gameId = splitted.first().replace("Game ", "")
        val sets = splitted[1].split(";")
        return Game(
            id = gameId.toInt(),
            sets = sets.map { set ->
                val reds = set.getColorCubes("red")
                val greens = set.getColorCubes("green")
                val blues = set.getColorCubes("blue")
                Round(reds,greens,blues,)
                    },
            gameRules = gameRules
        )
    }
    
    fun part1(input: List<String>, gameRules: GameRules): Int {
        
        return input.map { line ->
            parseGame(line, gameRules)
        }.filter { it.isPossible }.sumOf { it.id }
    }
    
    fun part2(input: List<String>, gameRules: GameRules): Int {
        return input.map { line ->
            val game = parseGame(line, gameRules = gameRules)
            val reduced = game.sets.reduce { acc, set ->
                Round(maxOf(acc.red, set.red), maxOf(acc.green, set.green), maxOf(acc.blue, set.blue))
            }
            game.copy(power = reduced.red * reduced.blue * reduced.green)
        }.sumOf { it.power }
    }


    val gameRules = GameRules(12,13,14)
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput, gameRules) == 8)
    check(part2(testInput, gameRules) == 2286)

    val input = readInput("Day02")
    part1(input, gameRules).println()
    part2(input, gameRules).println()
}

fun String.getColorCubes(color: String) = this.split(",")
    .firstOrNull { it.contains(color) }
    ?.replace(color,"")?.trim()?.toInt() ?: 0

data class GameRules(
    val maxRed: Int,
    val maxGreen: Int,
    val maxBlue: Int,
) {}

data class Game(
        val id: Int,
        val sets: List<Round>,
        val isPossible: Boolean,
        val power: Int = 0
) {
    constructor(id: Int, sets: List<Round>, gameRules: GameRules) : this(id, sets, isPossible = sets.isPossible(gameRules))
}

fun List<Round>.isPossible(gameRules: GameRules) =
        this.none { it.red > gameRules.maxRed || it.blue > gameRules.maxBlue || it.green > gameRules.maxGreen }

data class Round(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0,
)
