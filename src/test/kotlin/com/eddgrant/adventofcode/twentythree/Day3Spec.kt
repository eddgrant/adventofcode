package com.eddgrant.adventofcode.twentythree

import com.eddgrant.adventofcode.DataProvider
import com.eddgrant.adventofcode.DataType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class Day3Spec : StringSpec({

    "Part 1 : Test data: What is the sum of all of the part numbers in the engine schematic?" {
        val inputData = DataProvider.getData(2023, 3, 1, DataType.TEST)

        val sumOfPartNumbers = getSumOfPartNumbers(inputData)

        sumOfPartNumbers.shouldBeExactly(4361)
    }

    "Part 1 : Real data: What is the sum of all of the part numbers in the engine schematic?" {
        val inputData = DataProvider.getData(2023, 3, 1, DataType.REAL)

        val sumOfPartNumbers = getSumOfPartNumbers(inputData)

        println("The sum of the part numbers is: $sumOfPartNumbers")
    }

    "Part 2 : Test data: What is the sum of all of the gear ratios in your engine schematic?" {
        val inputData = DataProvider.getData(2023, 3, 1, DataType.TEST)

        val sumOfAllGearRatios = getSumOfAllGearRatios(inputData)

        sumOfAllGearRatios.shouldBeExactly(467835)
    }

    "Part 2 : Real data: What is the sum of all of the gear ratios in your engine schematic?" {
        val inputData = DataProvider.getData(2023, 3, 1, DataType.REAL)

        val sumOfAllGearRatios = getSumOfAllGearRatios(inputData)

        println("The sum of the gear ratios is: $sumOfAllGearRatios")
    }
})

private fun getSumOfPartNumbers(inputData: String): Int {
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
            (
                findPartNumbers(firstLineNumberMatches, firstLineSymbolMatches) +
                findPartNumbers(firstLineNumberMatches, secondLineSymbolMatches) +
                findPartNumbers(secondLineNumberMatches, firstLineSymbolMatches)
            )

        }
    println("Part numbers: $partNumbers")
    val sumOfPartNumbers = partNumbers.sum()
    return sumOfPartNumbers
}

private fun findPartNumbers(numberMatches: Sequence<MatchResult>, symbolMatches: Sequence<MatchResult>): Sequence<Int> =
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

private fun getSumOfAllGearRatios(inputData: String): Int {
    val numberMatcher = Regex("[0-9]+")
    val gearMatcher = Regex("[*]")

    val gearRatios = inputData
        .lines()
        .windowed(3, 1, true)
        .flatMapIndexed { index, lines ->
            val firstLineNumber = index + 1
            val secondLineNumber = index + 2
            val thirdLineNumber = index + 3

            println("\nProcessing lines $firstLineNumber, $secondLineNumber and $thirdLineNumber...")

            val firstLine: String = lines.getOrElse(0) { "" }
            val secondLine: String = lines.getOrElse(1) { "" }
            val thirdLine: String = lines.getOrElse(2) { "" }

            val firstLineNumberMatches = numberMatcher.findAll(firstLine)
            val firstLineGearMatches = gearMatcher.findAll(firstLine)

            val secondLineNumberMatches = numberMatcher.findAll(secondLine)
            val secondLineGearMatches = gearMatcher.findAll(secondLine)

            val thirdLineNumberMatches = numberMatcher.findAll(thirdLine)
            //val thirdLineGearMatches = gearMatcher.findAll(thirdLine)

            (
                findGearRatios(firstLineNumberMatches, firstLineGearMatches) + // Search entirely within the first line.
                findGearRatios(secondLineNumberMatches, firstLineGearMatches) + // Gears on first line, part numbers on 2nd line
                findGearRatios(firstLineNumberMatches, secondLineGearMatches) + // Numbers on first line, gears on 2nd line
                findGearRatios((firstLineNumberMatches + thirdLineNumberMatches), secondLineGearMatches) // Gears on 2nd line, numbers on first and / or third line
            )

        }
    println("Gear ratios: $gearRatios")
    val sumOfGearRatios = gearRatios.sum()
    return sumOfGearRatios
}

private fun findGearRatios(numberMatches: Sequence<MatchResult>, gearMatches: Sequence<MatchResult>): Sequence<Int> =
    gearMatches.map { gearMatch: MatchResult ->
        println("\n\tGear:  ${gearMatch.value} (index: ${gearMatch.range})")
        val gearIndex = gearMatch.range.first

        val partNumbersThatQualifyAsGearRatios = numberMatches.filter { numberMatch: MatchResult ->
            println("\n\tNumber:  ${numberMatch.value} (index: ${numberMatch.range})")

            val qualifyingPartNumberRange = numberMatch.range.first - 1..numberMatch.range.last + 1
            qualifyingPartNumberRange.contains(gearIndex)

        }
        .map { numberMatch ->
            numberMatch.value.toInt()
        }
        .toList()

        if(partNumbersThatQualifyAsGearRatios.size == 2) {
            println("Gear ratios: $partNumbersThatQualifyAsGearRatios")
            partNumbersThatQualifyAsGearRatios.first() * partNumbersThatQualifyAsGearRatios.last()
        }
        else {
            0
        }
    }



