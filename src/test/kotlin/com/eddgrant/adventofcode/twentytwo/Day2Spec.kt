package com.eddgrant.adventofcode.twentytwo

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly

enum class Outcome(val bonus: Int, val code: String) {
    WIN(6, "Z"),
    LOSE(0, "X"),
    DRAW(3, "Y");

    companion object {
        fun findByCode(code: String): Outcome? {
            return entries.find { it.code == code }
        }
    }
}

enum class Choice(val score: Int, val myCode: String, val opponentCode: String) {
    ROCK(1, "X", "A"),
    PAPER(2, "Y", "B"),
    SCISSORS(3, "Z", "C");

    companion object {
        fun findByMyCode(code: String): Choice? {
            return entries.find { it.myCode == code }
        }

        fun findByOpponentCode(code: String): Choice? {
            return entries.find { it.opponentCode == code }
        }

        fun battle(myChoice: Choice, opponentChoice: Choice): Int {
            val outcome = calculateOutcome(myChoice, opponentChoice)
            return outcome.bonus + myChoice.score
        }

        fun battle(opponentChoice: Choice, requiredOutcome: Outcome): Int {
            val myChoice = determineMyChoice(opponentChoice, requiredOutcome)
            return requiredOutcome.bonus + myChoice.score
        }

        fun determineMyChoice(opponentChoice: Choice, requiredOutcome: Outcome): Choice {
            return if(requiredOutcome == Outcome.DRAW) {
                opponentChoice
            } else {
                when(opponentChoice) {
                    ROCK -> if(requiredOutcome == Outcome.WIN) PAPER else SCISSORS
                    PAPER -> if(requiredOutcome == Outcome.WIN) SCISSORS else ROCK
                    SCISSORS -> if(requiredOutcome == Outcome.WIN) ROCK else PAPER
                }
            }
        }

        private fun calculateOutcome(myChoice: Choice, opponentChoice: Choice) : Outcome {
            return if (myChoice == opponentChoice) {
                Outcome.DRAW
            } else {
                when(myChoice) {
                    ROCK -> if (opponentChoice == SCISSORS) Outcome.WIN else Outcome.LOSE
                    PAPER -> if (opponentChoice == ROCK) Outcome.WIN else Outcome.LOSE
                    SCISSORS -> if (opponentChoice == PAPER) Outcome.WIN else Outcome.LOSE
                }
            }
        }
    }
}


class Day2Spec :StringSpec ({

    "Test Data: What would your total score be if everything goes exactly according to your strategy guide?" {
        val inputData = ElfUtils.getFileContent("/2022/day2/test-input.txt")
        val score = calculateScore(inputData)
        score.shouldBeExactly(15)
    }

    "Real Data: What would your total score be if everything goes exactly according to your strategy guide?" {
        val inputData = ElfUtils.getFileContent("/2022/day2/real-input.txt")
        val score = calculateScore(inputData)
        print("Score is: $score")
    }

    "Test Data: Following the Elf's instructions for the second column, what would your total score be if everything goes exactly according to your strategy guide?" {
        val inputData = ElfUtils.getFileContent("/2022/day2/test-input.txt")
        val score = calculateScoreUsingRequiredOutcome(inputData)
        score.shouldBeExactly(12)
    }

    "Real Data: Following the Elf's instructions for the second column, what would your total score be if everything goes exactly according to your strategy guide?" {
        val inputData = ElfUtils.getFileContent("/2022/day2/real-input.txt")
        val score = calculateScoreUsingRequiredOutcome(inputData)
        print("Score is: $score")
    }


})

private fun calculateScore(inputData: String) = inputData
    .lines().sumOf {
        val opponentChoice = Choice.findByOpponentCode(it[0].toString())!!
        val myChoice = Choice.findByMyCode(it[2].toString())!!
        Choice.battle(myChoice, opponentChoice)
    }

private fun calculateScoreUsingRequiredOutcome(inputData: String) = inputData
    .lines().sumOf {
        val opponentChoice = Choice.findByOpponentCode(it[0].toString())!!
        val requiredOutcome = Outcome.findByCode(it[2].toString())!!
        Choice.battle(opponentChoice, requiredOutcome)
    }