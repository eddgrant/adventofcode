package com.eddgrant.adventofcode.twentythree

import com.eddgrant.adventofcode.DataProvider
import com.eddgrant.adventofcode.DataType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import kotlin.math.max


enum class Colour {
    BLUE,
    GREEN,
    RED
}

data class Cube(val colour : Colour)

data class BagOfCubes(val contents: Map<Cube, Int>) {
    fun contains(soughtCubes: Map<Cube, Int>) : Boolean {
        return !soughtCubes.map { (cube, count) ->
            getCountByColour(cube.colour) >= count
        }.toSet().contains(false)
    }

    private fun getCountByColour(colour: Colour): Int = contents.map { (cube, count) -> cube.colour to count }.toMap().getOrElse(colour) { 0 }
}

data class GameResult(val gameId: Int, val cubeDraws: List<Map<Cube, Int>>)

class BagOfCubesGameResultParser {
    fun parseResults(gameResults : List<String>): List<GameResult> {
        return gameResults.map { parseGameResult(it) }
    }

    private fun parseGameResult(gameResult: String): GameResult {
        val tokens = gameResult.split(":")
        val gameId = tokens.first().split(" ").last().toInt()
        val cubesAndColours = tokens
            .last().split(";").map {
                it.split(",").map {
                    val countToColour = it.trim().split(" ")
                    Cube(Colour.valueOf(countToColour.last().uppercase())) to countToColour.first().toInt()
                }.toMap()
            }
        return GameResult(gameId, cubesAndColours)
    }
}


    class Day2Spec : StringSpec({

        val bagOfCubes = BagOfCubes(mapOf(Cube(Colour.BLUE) to 14, Cube(Colour.GREEN) to 13, Cube(Colour.RED) to 12))

        "Part 1 : Test data: What is the sum of the IDs of those games?" {
            val inputData = DataProvider.getData(2023, 2, 1, DataType.TEST)
            val sumOfIdsOfPlayableGames = calculateSumOfIdsOfPlayableGames(inputData, bagOfCubes)
            sumOfIdsOfPlayableGames.shouldBeExactly(8)
        }

        "Part 1 : Real data: What is the sum of the IDs of those games?" {
            val inputData = DataProvider.getData(2023, 2, 1, DataType.REAL)
            val sumOfIdsOfPlayableGames = calculateSumOfIdsOfPlayableGames(inputData, bagOfCubes)
            println("The sum of the ids of the playable games is: $sumOfIdsOfPlayableGames")
        }

        "Part 2 : Test data: What is the sum of the power of these sets?" {
            val inputData = DataProvider.getData(2023, 2, 1, DataType.TEST)
            val sumOfIdsOfPlayableGames = calculateSumOfPowerOfPlayableGames(inputData)
            sumOfIdsOfPlayableGames.shouldBeExactly(2286)
        }

        "Part 2 : Real data: What is the sum of the power of these sets?" {
            val inputData = DataProvider.getData(2023, 2, 1, DataType.REAL)
            val sumOfPowers = calculateSumOfPowerOfPlayableGames(inputData)
            println("The sum of the power of these sets is: $sumOfPowers")
        }
    })

private fun calculateSumOfIdsOfPlayableGames(inputData: String, bagOfCubes: BagOfCubes) =
    BagOfCubesGameResultParser()
        .parseResults(inputData.lines())
        .sumOf { gameResult ->
            val anyDrawsImpossible = gameResult.cubeDraws.map { cubeDraw ->
                bagOfCubes.contains(cubeDraw)
            }.toSet().contains(false)

            if (anyDrawsImpossible) {
                0
            } else {
                gameResult.gameId
            }
}

private fun calculateSumOfPowerOfPlayableGames(inputData: String) =
    BagOfCubesGameResultParser()
        .parseResults(inputData.lines())
        .map { gameResult ->
            gameResult.cubeDraws.fold(mapOf()) { acc: Map<Colour, Int>, cubeDraw: Map<Cube, Int> ->
                acc + cubeDraw.map { (cube, count) ->
                    cube.colour to max(count, acc.getOrDefault(cube.colour, count))
                }.toMap()
            }
        }.fold(0) { acc, coloursToCounts ->
            acc + coloursToCounts.values.fold(1) { accumulator, count ->
                accumulator * count
            }
        }
