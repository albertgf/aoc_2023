fun main() {
    fun part1(input: List<String>): List<Int> {
        val mapped = input.map { line ->
            val lineMapped = line.replace(Regex("[a-z]"), "")
            val first = lineMapped.first()
            val last = lineMapped.last()
            "$first$last".toInt()
        }
        return mapped
    }

    fun part2(input: List<String>): List<Int> {
        return input.map { line ->
            val lineMapped = line.replace("one","o1e")
                .replace("two","t2o")
                .replace("three","t3e")
                .replace("four","f4r")
                .replace("five","f5e")
                .replace("six","s6x")
                .replace("seven","s7n")
                .replace("eight","e8t")
                .replace("nine","n9n").replace(Regex("[a-z]"), "")
            val first = lineMapped.first()
            val last = lineMapped.last()

            "$first$last".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day01_test")
    //check(part1(testInput) == 1)

    val input = readInput("Day01")
    part1(input).sumOf { it }.println()
    part2(input).sumOf { it }.println()
}
