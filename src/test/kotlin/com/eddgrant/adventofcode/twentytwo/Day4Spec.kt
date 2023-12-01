package com.eddgrant.adventofcode.twentytwo

import com.eddgrant.adventofcode.twentytwo.ElfUtils.Companion.getFileContent
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class Day4Spec : StringSpec({
    "Part 1: Test data: In how many assignment pairs does one range fully contain the other?" {
        val fileContent = getFileContent("/2022/day4/test-input.txt")
        val actualNumberOfPairsWhereOneRangeEntirelyContainsTheOther : Int = countEntirelyOverlappingPairRanges(fileContent)

        val expectedNumberOfPairsWhereOneRangeEntirelyContainsTheOther = 2
        actualNumberOfPairsWhereOneRangeEntirelyContainsTheOther.shouldBeExactly(expectedNumberOfPairsWhereOneRangeEntirelyContainsTheOther)
    }

    "Part 1: Real data: In how many assignment pairs does one range fully contain the other?" {
        val fileContent = getFileContent("/2022/day4/real-input.txt")
        val numberOfPairsWhereOneRangeEntirelyContainsTheOther : Int = countEntirelyOverlappingPairRanges(fileContent)
        print("The number of pairs with entirely overlapping ranges is: $numberOfPairsWhereOneRangeEntirelyContainsTheOther")
    }

    "Part 2: Test data: In how many assignment pairs do the ranges overlap?" {
        val fileContent = getFileContent("/2022/day4/test-input.txt")
        val actualNumberOfPairsWhereOneRangesOverlap : Int = countAnyDegreeOfOverlappingPairRanges(fileContent)

        val expectedNumberOfPairsWhereOneRangeEntirelyContainsTheOther = 4
        actualNumberOfPairsWhereOneRangesOverlap.shouldBeExactly(expectedNumberOfPairsWhereOneRangeEntirelyContainsTheOther)
    }

    "Part 2: Real data: In how many assignment pairs do the ranges overlap?" {
        val fileContent = getFileContent("/2022/day4/real-input.txt")
        val actualNumberOfPairsWhereRangesOverlap : Int = countAnyDegreeOfOverlappingPairRanges(fileContent)
        print("The number of pairs with overlapping ranges is: $actualNumberOfPairsWhereRangesOverlap")

    }
})

private fun calculatePairRanges(fileContent: String): List<Pair<IntRange, IntRange>> = fileContent
    .lines()
    .map { pairSectionIds ->
        val sectionIds = pairSectionIds.split(",")
        val firstPairSectionIds = sectionIds.first().split("-").map { it.toInt() }
        val secondPairSectionIds = sectionIds.last().split("-").map { it.toInt() }
        val firstPairSectionIdsRange = firstPairSectionIds.first()..firstPairSectionIds.last()
        val secondPairSectionIdsRange = secondPairSectionIds.first()..secondPairSectionIds.last()
        Pair(firstPairSectionIdsRange, secondPairSectionIdsRange)
    }

private fun countEntirelyOverlappingPairRanges(fileContent: String) = calculatePairRanges(fileContent)
    .map { pairOfRanges: Pair<IntRange, IntRange> ->
        pairOfRanges.first.toSet().containsAll(pairOfRanges.second.toSet()) ||
            pairOfRanges.second.toSet().containsAll(pairOfRanges.first.toSet())
    }.fold(0) { acc, value ->
        if (value) acc + 1 else acc
    }

private fun countAnyDegreeOfOverlappingPairRanges(fileContent: String) = calculatePairRanges(fileContent)
    .map { pairOfRanges: Pair<IntRange, IntRange> ->
        pairOfRanges.first.toSet().intersect(pairOfRanges.second.toSet()).isNotEmpty() ||
                pairOfRanges.second.toSet().intersect(pairOfRanges.first.toSet()).isNotEmpty()
    }.fold(0) { acc, value ->
        if (value) acc + 1 else acc
    }