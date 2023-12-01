package com.eddgrant.adventofcode.twentytwo

import java.util.*

class ElfUtils {

    companion object {
        fun getCaloriesMappedToElfId(input: String): SortedMap<Int, Int> {
            val caloriesMappedToElfNumbers = input
                .split("\n")
                .fold(
                    mutableListOf(mutableListOf())
                ) { acc: MutableList<MutableList<Int>>, calorieValue: String ->
                    try {
                        acc.last().add(calorieValue.toInt())
                    } catch (_: NumberFormatException) {
                        acc.add(mutableListOf())
                    }
                    acc
                }
                .map { it.sum() }
                .withIndex().associateBy({ it.value }, { it.index + 1 })
                .toSortedMap()
            return caloriesMappedToElfNumbers
        }

        fun getFileContent(inputFile: String) = ElfUtils::class.java.getResource(inputFile)!!.readText(Charsets.UTF_8)
    }
}