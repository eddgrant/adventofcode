package com.eddgrant.adventofcode.twentytwo

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day6 : StringSpec({

    val testFileContent = ElfUtils.getFileContent("/2022/day6/test-input.txt").lines().first()
    val realFileContent = ElfUtils.getFileContent("/2022/day6/real-input.txt").lines().first()

    "Part 1: Test data: How many characters need to be processed before the first start-of-packet marker is detected?" {
        val expectedResult = 7
        val firstBlockWithoutCharacterRepetition = testFileContent
            .windowed(4, 1, true)
            .first {
                it.toSet().size == 4
            }
        val actualResult = testFileContent.indexOf(firstBlockWithoutCharacterRepetition) + 4
        actualResult.shouldBe(expectedResult)
    }

    "Part 1: Real data: How many characters need to be processed before the first start-of-packet marker is detected?" {
        val firstBlockWithoutCharacterRepetition = realFileContent
            .windowed(4, 1, true)
            .first {
                it.toSet().size == 4
            }
        val actualResult = realFileContent.indexOf(firstBlockWithoutCharacterRepetition) + 4
        print("The answer is $actualResult")
    }
})