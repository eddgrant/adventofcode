package com.eddgrant.adventofcode.twentytwo

import com.eddgrant.adventofcode.twentytwo.ElfUtils.Companion.getCaloriesMappedToElfId
import com.eddgrant.adventofcode.twentytwo.ElfUtils.Companion.getFileContent
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

class Day1Spec : StringSpec({

    "find the elf carrying the most calories, how many total calories is that elf carrying?" {
        val input = getFileContent("/2022/day1/input.txt")

        val caloriesMappedToElfNumbers = getCaloriesMappedToElfId(input)

        val highestCalories = caloriesMappedToElfNumbers.lastKey()
        val elfWithHighestCalories = caloriesMappedToElfNumbers.get(highestCalories)

        print("The Elf with the most calories was Elf number $elfWithHighestCalories who had $highestCalories calories")

        elfWithHighestCalories!!.shouldBeExactly(69)
        highestCalories.shouldBeExactly(71023)
    }

    "Find the top three Elves carrying the most Calories" {
        val input = ElfUtils.getFileContent("/2022/day1/input.txt")

        val calories = ElfUtils.getCaloriesMappedToElfId(input)
            .keys
            .sortedDescending()
            .take(3)
            .sum()


        print("The combined calories for the top 3 Elves is: $calories")

        calories.shouldBeExactly(206289)
    }
})