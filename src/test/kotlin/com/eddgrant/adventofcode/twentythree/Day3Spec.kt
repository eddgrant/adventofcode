package com.eddgrant.adventofcode.twentythree

import com.eddgrant.adventofcode.DataProvider
import com.eddgrant.adventofcode.DataType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class Day3Spec : StringSpec({

    "Part 1 : Test data: What is the sum of all of the part numbers in the engine schematic?" {
        val inputData = DataProvider.getData(2023, 3, 1, DataType.TEST)

        val sumOfPartNumbers = getSumOfPartNUmbers(inputData)

        sumOfPartNumbers.shouldBeExactly(4361)
    }

    "Part 1 : Real data: What is the sum of all of the part numbers in the engine schematic?" {
        val inputData = DataProvider.getData(2023, 3, 1, DataType.REAL)

        val sumOfPartNumbers = getSumOfPartNUmbers(inputData)

        println("The sum of the part numbers is: $sumOfPartNumbers")
    }
})

private fun getSumOfPartNUmbers(inputData: String): Int {
    val numberMatcher = Regex("[0-9]+")
    val symbolMatcher = Regex("[^0-9.\\n]")

    val partNumbers = inputData
        .lines()
        .windowed(2, 1, true)
        .flatMapIndexed { index, lines ->
            val firstLineNumber = index + 1
            val secondLineNumber = index + 2
            println("\nProcessing lines $firstLineNumber and $secondLineNumber...")

            val firstLine: String = lines.getOrElse(0) { "" }
            val secondLine: String = lines.getOrElse(1) { "" }

            val firstLineNumberMatches = numberMatcher.findAll(firstLine)
            val firstLineSymbolMatches = symbolMatcher.findAll(firstLine)
            val secondLineNumberMatches = numberMatcher.findAll(secondLine)
            val secondLineSymbolMatches = symbolMatcher.findAll(secondLine)
            findPartNumbers(firstLineNumberMatches, firstLineSymbolMatches) +
                    findPartNumbers(firstLineNumberMatches, secondLineSymbolMatches) +
                    findPartNumbers(secondLineNumberMatches, firstLineSymbolMatches)

        }
    println("Part numbers: $partNumbers")
    val sumOfPartNumbers = partNumbers.sum()
    return sumOfPartNumbers
}

fun findPartNumbers(numberMatches: Sequence<MatchResult>, symbolMatches: Sequence<MatchResult>): Sequence<Int> =
    numberMatches.mapNotNull { numberMatch ->
        //println("\n\tNumber:  ${numberMatch.value} (index: ${numberMatch.range})")

        symbolMatches.firstNotNullOfOrNull { symbolMatch ->
            //println("\tSymbol: ${symbolMatch.value} (index: ${symbolMatch.range})")

            // Minus 1 and plus 1 to allow for adjacent matches.
            val qualifyingPartNumberRange = numberMatch.range.first - 1..numberMatch.range.last + 1
            val symbolIndex = symbolMatch.range.first
            if(qualifyingPartNumberRange.contains(symbolIndex)) {
                println("${numberMatch.value} is a part number")
                numberMatch.value.toInt()
            } else {
                null
            }
        }
    }


