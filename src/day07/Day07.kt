import java.util.regex.Pattern

fun main() {
    fun getHand(input: String, mapper: Mapper, joker: Boolean = false) : Hand {
        val mapCards = HashMap<Char, Char>().apply {
            put('A','a')
            put('K','b')
            put('Q','c')
            put('J',if (joker) 'n' else 'd')
            put('T','e')
            put('9','f')
            put('8','g')
            put('7','h')
            put('6','i')
            put('5','j')
            put('4','k')
            put('3','l')
            put('2','m')
        }
        val (hand, bid) = input.split(" ")
        val handMapped = hand.map { mapCards[it] }.joinToString("")
        val handKind = handMapped.getHandKind(joker, mapper)
        return Hand(handMapped, bid.toInt(), handKind)
    }

    fun part1(input: List<String>): Int {
        return input.map { getHand(it, Mapper()) }.sorted().mapIndexed { index, hand -> hand.bid * (index+1) }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { getHand(it, Mapper(), joker = true) }.sorted().mapIndexed { index, hand -> hand.bid * (index+1) }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("day07/Day07")

    timetrack {
        part1(input).println()
    }

    timetrack {
        part2(input).println()
    }
}

enum class HandKind {
    HIGH,TWO,TWOTWO, THREE, FULLHOUSE, FOUR,FIVE, 
}

data class Hand(
    val value: String,
    val bid: Int,
    val handkind: HandKind
) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int =
            if (handkind != other.handkind)
                handkind.ordinal - other.handkind.ordinal
            else
                value.compareTo(other.value) * -1
}

data class Mapper(
    val joker: Regex = Regex("(n)\\1{0}"),
    val repoker: Regex = Regex("([a-m])\\1{4}"),
    val poker: Regex = Regex("([a-m])\\1{3}"),
    val triplet: Regex = Regex("([a-m])\\1{2}"),
    val pair: Regex = Regex("([a-m])\\1{1}"),
)


fun String.getHandKind(joker: Boolean, mapper: Mapper) : HandKind {
    val sorted = this.toCharArray().sorted().joinToString("")
    val jokers = mapper.joker.findAll(sorted).count().toInt()
    val count = when {
        mapper.repoker.findAll(sorted).count() > 0 -> 5
        mapper.poker.findAll(sorted).count() > 0 -> 4
        mapper.triplet.findAll(sorted).count() > 0 -> 3
        mapper.pair.findAll(sorted).count() > 0 -> 2
        else -> 1
    }
    
    return when (count + jokers) {
        5 -> HandKind.FIVE
        4 -> HandKind.FOUR
        3 -> if (mapper.pair.findAll(sorted).count() == 2) HandKind.FULLHOUSE else HandKind.THREE
        2 -> if (mapper.pair.findAll(sorted).count() == 2) HandKind.TWOTWO else HandKind.TWO
        else -> HandKind.HIGH
    }
}