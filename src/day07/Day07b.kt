fun main() {
    fun List<String>.solve(jokers: Boolean) = map {
        val (cards, bid) = it.split(" ")
        Draw(cards, bid.toInt(), jokers)
    }
        .sorted()
        .mapIndexed { index, hand -> (index + 1) * hand.bid }
        .sum()

    fun part1(input: List<String>) : Int {
        return input.map {
            val (cards, bid) = it.split(" ")
            Draw(cards, bid.toInt(), joker = false)
        }.sorted().mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    fun part2(input: List<String>) = input.solve(true)

    val testInput = readInput("day07/Day07_test")

    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("day07/Day07")

    timetrack { part1(input).println() }
    timetrack { part2(input).println() }
}

enum class Kind {
    HIGH,TWO,TWOTWO, THREE, FULLHOUSE, FOUR,FIVE, 
}

data class Draw(
    val cards: String,
    val bid: Int,
    val joker: Boolean
) : Comparable<Draw> {
    private val cardMap = mapOf('2' to 2, '3' to 3, '4' to 4, '5' to 5, '6' to 6, '7' to 7, '8' to 8, '9' to 9, 'T' to 10, 'J' to if (joker) 1 else 11, 'Q' to 12, 'K' to 13, 'A' to 14)
    private val cardValues = cards.map { cardMap[it]!! }

    private fun Char.isJoker() = joker && this == 'J'

    private val jokers = cards.count { it.isJoker() }
    private val kind = cards.filter { !it.isJoker() }
        .associateWith { card -> cards.count { it == card } }
        .values.sortedDescending()
        .let { sorted -> sorted.getOrElse(0) { 0 } to sorted.getOrElse(1) { 0 } }
        .let { (highest, second) ->
            when (highest + jokers) {
                5 -> HandKind.FIVE
                4 -> HandKind.FOUR
                3 -> if (second == 2) HandKind.FULLHOUSE else HandKind.THREE
                2 -> if (second == 2) HandKind.TWOTWO else HandKind.TWO
                else -> HandKind.HIGH
            }
        }

    override fun compareTo(other: Draw) =
        if (kind != other.kind) kind.ordinal - other.kind.ordinal
        else cardValues.zip(other.cardValues) { c, o -> c - o }.firstOrNull { it != 0 } ?: 0
}