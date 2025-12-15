package com.eddgrant.adventofcode.twentytwo

import com.eddgrant.adventofcode.twentytwo.CrateMover.Companion.movementStatementRegex
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Day5Spec : StringSpec({

    val testFileContent = ElfUtils.getFileContent("/2022/day5/test-input.txt")
    val realFileContent = ElfUtils.getFileContent("/2022/day5/real-input.txt")

    "Part 1: Test data: After the rearrangement procedure completes, what crate ends up on top of each stack?" {
        val instructions = testFileContent.split("\n\n")
        val initialCrateLayout = instructions.first()
        val crateMovements = instructions.last()
        val cim = CrateMover9000(initialCrateLayout)
        cim.moveCrates(crateMovements)
        val topCrateIdentifiers = cim.getTopCrateIdentifiers()
        topCrateIdentifiers.shouldBe("CMZ")
    }

    "Part 1: Real data: After the rearrangement procedure completes, what crate ends up on top of each stack?" {
        val instructions = realFileContent.split("\n\n")
        val initialCrateLayout = instructions.first()
        val crateMovements = instructions.last()
        val cim = CrateMover9000(initialCrateLayout)
        cim.moveCrates(crateMovements)
        val topCrateIdentifiers = cim.getTopCrateIdentifiers()
        print("The crates at the top of each stack are: $topCrateIdentifiers")
    }

    "Part 2: Test data: After the rearrangement procedure completes, what crate ends up on top of each stack?" {
        val instructions = testFileContent.split("\n\n")
        val initialCrateLayout = instructions.first()
        val crateMovements = instructions.last()
        val cim = CrateMover9001(initialCrateLayout)
        cim.moveCrates(crateMovements)
        val topCrateIdentifiers = cim.getTopCrateIdentifiers()
        topCrateIdentifiers.shouldBe("MCD")
    }

    "Part 2: Real data: After the rearrangement procedure completes, what crate ends up on top of each stack?" {
        val instructions = realFileContent.split("\n\n")
        val initialCrateLayout = instructions.first()
        val crateMovements = instructions.last()
        val cim = CrateMover9001(initialCrateLayout)
        cim.moveCrates(crateMovements)
        val topCrateIdentifiers = cim.getTopCrateIdentifiers()
        print("The crates at the top of each stack are: $topCrateIdentifiers")
    }
})

data class MoveInstruction(val quantity: Int, val sourceStackNumber: Int, val destinationStackNumber: Int)

data class Crate(val identifier: String)
class Stack {

    private val crates = mutableListOf<Crate>()

    fun addCrate(crate: Crate) {
        crates += listOf(crate)
    }

    fun removeTopCrate() : Crate = crates.removeLast()

    fun peekTopCrate() : Crate = crates.last()
}

interface CrateMover {
    fun moveCrates(crateMovements: String)

    companion object {
        val movementStatementRegex = Regex("^move ([0-9]+) from ([0-9]+) to ([0-9]+)\$")
    }
}

abstract class AbstractCrateMover(initialCrateLayout: String) : CrateMover {

    private val stacks: List<Stack>

    init {
        stacks = populateInitialCratePositions(initialCrateLayout)
    }

    private fun populateInitialCratePositions(initialCrateLayout: String): List<Stack> {
        val numberOfStacks = initialCrateLayout.lines().last().split(" ").filter { it != "" }.size
        val crateArrangements = initialCrateLayout.lines().subList(0, initialCrateLayout.lines().size - 1).reversed()
        val emptyCrateContainers: List<Stack> = (1..numberOfStacks).map { Stack() }

        return crateArrangements.foldIndexed(emptyCrateContainers) { _, acc: List<Stack>, crateArrangementLayer: String ->
            val stackTraversalRange = 0..< numberOfStacks * CRATE_DEFINITION_CHARACTER_WIDTH step CRATE_DEFINITION_CHARACTER_WIDTH
            stackTraversalRange.forEachIndexed { stackIndex, crateStartingIndex ->
                try {
                    val crateDefinition = crateArrangementLayer.substring(crateStartingIndex, crateStartingIndex + CRATE_DEFINITION_CHARACTER_WIDTH - 1)
                    if(crateDefinition.trim() != "") {
                        val indexOfCrateIdentifierWithinCrateDefinition = 1
                        val crate = Crate(identifier = crateDefinition[indexOfCrateIdentifierWithinCrateDefinition].toString())
                        acc[stackIndex].addCrate(crate)
                    }
                }
                catch (_: IndexOutOfBoundsException) { }
            }
            acc
        }
    }

    protected fun moveCrateBetweenStacks(sourceStackNumber : Int, destinationStackNumber : Int) {
        val crate = removeCrateFromStack(sourceStackNumber)
        addCratetoStack(destinationStackNumber, crate)
    }

    protected fun addCratetoStack(destinationStackNumber: Int, crate: Crate) {
        stacks[destinationStackNumber - 1].addCrate(crate)
        println("Crate ${crate.identifier} added from the top of stack $destinationStackNumber")
    }

    protected fun removeCrateFromStack(sourceStackNumber: Int): Crate {
        val crate = stacks[sourceStackNumber - 1].removeTopCrate()
        println("Crate ${crate.identifier} removed from the top of stack $sourceStackNumber")
        return crate
    }

    fun getTopCrateIdentifiers() : String = stacks.joinToString(separator = "") { it.peekTopCrate().identifier }

    companion object {
        /**
         * The number of characters that represents a Crate definition in the input file.
         *
         * Crates are identified by an identifier contained by square brackets e.g.
         *
         * ```
         * [x]
         * ```
         *
         * Where more than 1 Crate is represented in a line, each one is separated by a single space character e.g.
         *
         * ```
         * [x] [y] [z]
         * ```
         *
         * There is no space character after the last Crate
         */
        private const val CRATE_DEFINITION_CHARACTER_WIDTH = 4

    }

}

class CrateMover9000(private val initialCrateLayout: String) : AbstractCrateMover(initialCrateLayout) {
    override fun moveCrates(crateMovements: String) {
        crateMovements.lines().forEach() { movementStatement ->
            val result = movementStatementRegex.find(movementStatement)!!
            val moveInstruction = result.destructured.let {
                (n, s, d) -> MoveInstruction(n.toInt(), s.toInt(), d.toInt())
            }
            repeat(moveInstruction.quantity) {
                moveCrateBetweenStacks(moveInstruction.sourceStackNumber, moveInstruction.destinationStackNumber)
            }
        }
    }
}

class CrateMover9001(private val initialCrateLayout: String) : AbstractCrateMover(initialCrateLayout) {
    override fun moveCrates(crateMovements: String) {
        crateMovements.lines().forEach() { movementStatement ->
            val result = movementStatementRegex.find(movementStatement)!!
            val moveInstruction = result.destructured.let {
                    (n, s, d) -> MoveInstruction(n.toInt(), s.toInt(), d.toInt())
            }
            (1..moveInstruction.quantity)
                .map { removeCrateFromStack(moveInstruction.sourceStackNumber) }
                .reversed()
                .forEach { addCratetoStack(moveInstruction.destinationStackNumber, it)}
        }
    }
}