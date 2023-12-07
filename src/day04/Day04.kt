import kotlin.math.min
import kotlin.math.pow

fun main() {
    
    fun parserCard(input: String) : ScracthCard {
        val gameIdValues = input.splitToPair(":")
        val gameId = gameIdValues.first.replace("Card", "").trim().toInt()
        val valuesResults = gameIdValues.second.splitToPair("|")
        val values = valuesResults.first.split(" ").filterNot { it == " " || it.isEmpty() }.map { it.toInt() }.toSet()
        val results = valuesResults.second.split(" ").filterNot { it == " " || it.isEmpty() }.map { it.toInt() }.toSet()
        val result = values.intersect(results).sumOf { x -> min(values.count { it == x }, results.count { it == x }) }
        return ScracthCard(gameId, result)
    }
    

    fun part1(input: List<String>): Int {
        return input.map { parserCard(it) }.map { (if (it.winningNumbers > 0)2.0.pow(it.winningNumbers-1) else 0).toInt() }.sumOf { it }
    }


    fun part2(input: List<String>): Int {
        val hashMap = input.map { parserCard(it) }.associateBy ({ it.id }, {it})
        hashMap.forEach { (key, card) ->
            if (card.winningCards > 0) {
                for (i in 1..card.winningNumbers) {
                    hashMap[key + i]?.addWinningCards(card.winningCards)
                }
            }
        }
        return hashMap.toList().sumOf { it.second.winningCards }
    }
    
    val cardInput = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"
    val testCard = parserCard(cardInput)
    check(testCard.id == 1)
    check(testCard.winningNumbers == 4)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
    
    
}

fun String.splitToPair(divider: String) = this.split(divider).let {
    Pair(it.first(), it.getOrNull(1) ?: "")
}

data class ScracthCard(
    val id: Int,
    val winningNumbers: Int,
    var winningCards: Int = 1,
) {
    fun addWinningCards(num: Int) {
        winningCards += num
    } 
}




