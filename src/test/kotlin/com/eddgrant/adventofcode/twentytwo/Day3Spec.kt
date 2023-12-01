package com.eddgrant.adventofcode.twentytwo

import com.eddgrant.adventofcode.twentytwo.ElfUtils.Companion.getFileContent
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class Day3Spec : StringSpec({

    fun getItemCodeToPriorityMappings(): Map<Char, Int> {
        val itemCodesToPriorities = mutableMapOf<Char, Int>()
        var index = 1
        for (letter in 'a'..'z') {
            itemCodesToPriorities[letter] = index
            index++
        }
        index = 27
        for (letter in 'A'..'Z') {
            itemCodesToPriorities[letter] = index
            index++
        }
        return itemCodesToPriorities
    }

    fun calculatePriorities(allRucksackContents: String): Int =
        allRucksackContents.lines().sumOf { rucksackContents: String ->
            val compartment1Contents = rucksackContents.take(rucksackContents.length / 2)
            val compartment2Contents = rucksackContents.takeLast(rucksackContents.length / 2)
            val itemsInBothCompartments: Set<Char> =
                compartment1Contents.toSet().intersect(compartment2Contents.toSet())
            val itemCodesToPriorities = getItemCodeToPriorityMappings()
            itemsInBothCompartments.sumOf { itemCodesToPriorities[it]!!.toInt() }
        }

    fun calculateSumOfBadgePriorities(allElfGroupsRucksackContents: String): Int {
        val itemCodeToPriorityMappings = getItemCodeToPriorityMappings()
        val sumOfBadgeItemTypePriorities = allElfGroupsRucksackContents.lines().chunked(3)
            .map { rucksackContentsString -> rucksackContentsString.map { it.toSet() } }
            .flatMap {
                it.zipWithNext { a, b -> a.intersect(b) }
                  .zipWithNext { a, b -> a.intersect(b) }
                  .elementAt(0)
            }
        .sumOf { itemCodeToPriorityMappings[it]!! }
        return sumOfBadgeItemTypePriorities
    }


    "Test data: Find the item type that appears in both compartments of each rucksack. What is the sum of the priorities of those item types?" {
        val allRucksackContents = getFileContent("/2022/day3/test-input.txt")
        val actualTotal = calculatePriorities(allRucksackContents)

        val expectedTotal = 157
        expectedTotal.shouldBeExactly(actualTotal)
    }

    "Real data: Find the item type that appears in both compartments of each rucksack. What is the sum of the priorities of those item types?" {
        val allRucksackContents = getFileContent("/2022/day3/real-input.txt")
        val actualTotal = calculatePriorities(allRucksackContents)
        print("The priority total is: $actualTotal")
    }

    "Part 2: Test data: What is the sum of the priorities of those item types?" {
        val allElfGroupsRucksackContents = getFileContent("/2022/day3/test-input.txt")
        val sumOfBadgePriorities = calculateSumOfBadgePriorities(allElfGroupsRucksackContents)
        sumOfBadgePriorities.shouldBeExactly(70)
    }


    "Part 2: Real data: What is the sum of the priorities of those item types?" {
        val allElfGroupsRucksackContents = getFileContent("/2022/day3/real-input.txt")
        val sumOfBadgeItemTypePriorities = calculateSumOfBadgePriorities(allElfGroupsRucksackContents)
        print("The sum of the badge priorities is: $sumOfBadgeItemTypePriorities")
    }
})


