package com.eddgrant.adventofcode.twentythree

import com.eddgrant.adventofcode.DataProvider
import com.eddgrant.adventofcode.DataType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe

class Day4Spec : StringSpec({

    "Part 1 : Test data: How many points are they worth in total?" {
        val inputData = DataProvider.getData(2023, 4, 1, DataType.TEST)
        val cards = inputData.lines().map { Card.fromSpec(it) }
        val sumOfPoints = cards.sumOf { it.calculatePoints() }
        sumOfPoints.shouldBeExactly(13)
    }

    "Part 1 : Real data: How many points are they worth in total?" {
        val inputData = DataProvider.getData(2023, 4, 1, DataType.REAL)
        val cards = inputData.lines().map { Card.fromSpec(it) }
        val sumOfPoints = cards.sumOf { it.calculatePoints() }
        println("The sum of the cards' points is: $sumOfPoints")
    }

    "Part 2 : Test data: How many total scratchcards do you end up with?" {
        val inputData = DataProvider.getData(2023, 4, 1, DataType.TEST)
        val cardRegistry = CardRegistry(inputData)
        val cardCount = cardRegistry.countScratchCardsUsingPartTwoRules()
        println("Total number of scratchcards is: $cardCount")
        cardCount.shouldBeExactly(30)
    }

    "Part 2 : Real data: How many total scratchcards do you end up with?" {
        val inputData = DataProvider.getData(2023, 4, 1, DataType.REAL)
        val cardRegistry = CardRegistry(inputData)
        val cardCount = cardRegistry.countScratchCardsUsingPartTwoRules()
        println("Total number of scratchcards is: $cardCount")
    }

    "We can create a Card from a spec" {
        val spec = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"
        val card = Card.fromSpec(spec)

        card.id.shouldBe(1)
        card.calculatePoints().shouldBeExactly(8)
    }

    "A Card can calculate its points value" {
        val card = Card(1, setOf(41, 48, 83, 86, 17), setOf(83,86,6,31,17,9,48,53))
        card.calculatePoints().shouldBeExactly(8)
    }

    "A Card can calculate its number of matching numbers" {
        val card = Card(1, setOf(41, 48, 83, 86, 17), setOf(83,86,6,31,17,9,48,53))
        card.countMatchingNumbers().shouldBeExactly(4)
    }

})

data class Card(val id: Int, val winningNumbers: Set<Int>, val numbers: Set<Int>) {
    fun calculatePoints() : Int  = matchingNumbers().fold(0) { acc, _ ->
        if(acc == 0) 1 else acc * 2
    }

    fun matchingNumbers(): Set<Int> = numbers.intersect(winningNumbers)

    fun countMatchingNumbers(): Int = matchingNumbers().size

    companion object {

        fun fromSpec(spec: String) : Card {
            val (cardIdAndWinningNumbers, numbersString) = spec.split("|")
            val (cardIdString, winningNumbersString) = cardIdAndWinningNumbers.split(":")
            val cardId = cardIdString.removePrefix("Card ").trim().toInt()
            val numbersRegex = Regex("[0-9]+")
            val winningNumbers = numbersRegex.findAll(winningNumbersString).map { it.value.toInt() }.toSet()
            val numbers = numbersRegex.findAll(numbersString).map { it.value.toInt() }.toSet()
            return Card(cardId, winningNumbers, numbers)
        }
    }
}

class CardRegistry(private val spec: String) {

    private val specCardsById : Map<Int, Card> = spec.lines().associate {
        val card = Card.fromSpec(it)
        card.id to card
    }

    fun countScratchCardsUsingPartTwoRules(): Int = specCardsById.values
        .fold(emptySequence<Card>()) { acc, card ->
            acc + getWinningCards(card)
        }.toList().size

    private fun getWinningCards(card: Card) : Sequence<Card> {
        val matchingNumbers = card.matchingNumbers()
        val cardIdsWonByThisCard = when {
            matchingNumbers.isEmpty() -> IntRange.EMPTY
            else -> (card.id + 1..(card.id + matchingNumbers.size))
        }

        println("Card: ${card.id}, cardIdsWonByThisCard: ${cardIdsWonByThisCard.toSet()}")

        return cardIdsWonByThisCard.map {
            specCardsById[it]!!
        }.fold(sequenceOf(card)) { acc, winningCard ->
            acc + getWinningCards(winningCard)
        }
    }
}