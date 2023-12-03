package com.eddgrant.adventofcode.twentythree

import com.eddgrant.adventofcode.DataProvider
import com.eddgrant.adventofcode.DataType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

val allNumberWordsAndDigits = Regex("(?=(zero|one|two|three|four|five|six|seven|eight|nine|\\d))")

class Day1Spec : StringSpec({

    "Part 1: Test data: Consider your entire calibration document. What is the sum of all of the calibration values?" {
        val inputData = DataProvider.getData(2023, 1, 1, DataType.TEST)
        val sumOfAllCalibrationValues = calculateSumOfCalibrationValues(inputData)
        println("The sum of all the calibration values is: $sumOfAllCalibrationValues")
        sumOfAllCalibrationValues.shouldBeExactly(142)
    }

    "Part 1: Real data: Consider your entire calibration document. What is the sum of all of the calibration values?" {
        val inputData = DataProvider.getData(2023, 1, 1, DataType.REAL)
        val sumOfAllCalibrationValues = calculateSumOfCalibrationValues(inputData)
        println("The sum of all the calibration values is: $sumOfAllCalibrationValues")
    }

    "Part 2: Test data: Consider your entire calibration document. What is the sum of all of the calibration values?" {
        val inputData = DataProvider.getData(2023, 1, 2, DataType.TEST)
        val sumOfAllCalibrationValues = calculateSumOfCalibrationValues(inputData)
        println("The sum of all the calibration values is: $sumOfAllCalibrationValues")
        sumOfAllCalibrationValues.shouldBeExactly(281)
    }

    "Part 2: Real data: Consider your entire calibration document. What is the sum of all of the calibration values?" {
        val inputData = DataProvider.getData(2023, 1, 1, DataType.REAL)
        val sumOfAllCalibrationValues = calculateSumOfCalibrationValues(inputData)
        println("The sum of all the calibration values is: $sumOfAllCalibrationValues")
    }
})

private fun calculateSumOfCalibrationValues(inputData: String) = inputData.lines().sumOf { line ->
    val digitsAsWordsOrDigits = allNumberWordsAndDigits.findAll(line).toList()
    val firstDigit = Digit.fromString(digitsAsWordsOrDigits.first().groups.last()!!.value).intValue
    val secondDigit = Digit.fromString(digitsAsWordsOrDigits.last().groups.last()!!.value).intValue
    "${firstDigit}${secondDigit}".toInt()
}