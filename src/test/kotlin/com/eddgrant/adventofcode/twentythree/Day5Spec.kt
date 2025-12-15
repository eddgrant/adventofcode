    package com.eddgrant.adventofcode.twentythree

import com.eddgrant.adventofcode.DataProvider
import com.eddgrant.adventofcode.DataType
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.min

    class Day5Spec : StringSpec({

    "Part 1 : Test data: What is the lowest location number that corresponds to any of the initial seed numbers?" {
        val inputData = DataProvider.getData(2023, 5, 1, DataType.TEST)
        val locationResolver = LocationResolver.fromSpec(inputData, false)
        val lowestLocationNumber = locationResolver.getLowestLocationNumber()
        lowestLocationNumber.shouldBe(35)
    }

    "Part 1 : Real data: What is the lowest location number that corresponds to any of the initial seed numbers?" {
        val inputData = DataProvider.getData(2023, 5, 1, DataType.REAL)
        val locationResolver = LocationResolver.fromSpec(inputData, true)
        val lowestLocationNumber = locationResolver.getLowestLocationNumber()
        //println("Lowest location number is: $lowestLocationNumber")
    }

    "Part 2 : Test data: What is the lowest location number that corresponds to any of the initial seed numbers?" {
        val inputData = DataProvider.getData(2023, 5, 1, DataType.TEST)
        val locationResolver = LocationResolver.fromSpec(inputData, true)
        val lowestLocationNumber = locationResolver.getLowestLocationNumber()
        lowestLocationNumber.shouldBe(46)
    }

    "Part 2 : Real data: What is the lowest location number that corresponds to any of the initial seed numbers?" {
        val inputData = DataProvider.getData(2023, 5, 1, DataType.REAL)
        val locationResolver = LocationResolver.fromSpec(inputData, true)
        val lowestLocationNumber = locationResolver.getLowestLocationNumber()
        println("Lowest location number is: $lowestLocationNumber")
    }

    "Test getSeeds" {
        val inputData = DataProvider.getData(2023, 5, 1, DataType.REAL)
        val locationResolver = LocationResolver.fromSpec(inputData, true)
        println(locationResolver.getSeeds().last())
    }

})

class LocationResolver private constructor(
    val seedNumbers : Sequence<Long>?,
    val seedRanges : Sequence<LongRange>?,
    val seedToSoilResolver : SourceToDestinationResolver,
    val soilToFertiliserResolver : SourceToDestinationResolver,
    val fertiliserToWaterResolver : SourceToDestinationResolver,
    val waterToLightResolver : SourceToDestinationResolver,
    val lightToTemperatureResolver : SourceToDestinationResolver,
    val temperatureToHumidityResolver : SourceToDestinationResolver,
    val humidityToLocationResolver : SourceToDestinationResolver,
) {

    fun getSeeds(): Sequence<Long> = seedNumbers!!.ifEmpty {
        seedRanges!!.flatMap {
            it.map { seedValue ->
                seedValue
            }
        }
    }

    var count = 3L
    /*fun getSeedsNicely(): Sequence<Long> = generateSequence {
        testif(seedRanges!!.iterator().hasNext()) {
            var currentRange = seedRanges.iterator().next()
            if(currentRange.iterator().hasNext()) {
                currentRange.iterator().next()
            }
            else {
                seedRanges.iterator().next()
            }
        }



        //(count--).takeIf { it > 0 } // will return null, when value becomes non-positive,
        // and that will terminate the sequence
    }*/


    fun getLowestLocationNumber() : Long = getSeeds()
            .map { seedToSoilResolver.resolve(it) }
            .map { soilToFertiliserResolver.resolve(it) }
            .map { fertiliserToWaterResolver.resolve(it) }
            .map { waterToLightResolver.resolve(it) }
            .map { lightToTemperatureResolver.resolve(it) }
            .map { temperatureToHumidityResolver.resolve(it) }
            .map { humidityToLocationResolver.resolve(it) }
            .fold(0L) { acc, current ->
                //println("Comparing $current and $acc")
                if(acc == 0L) current else { min(current, acc) }
            }

    companion object {

        val seedsSectionRegexp = Regex("^seeds:.*$")
        val seedsToSoilSectionRegexp = Regex("^seed-to-soil map:$")
        val soilToFertiliserSectionRegexp = Regex("^soil-to-fertilizer map:$")
        val fertiliserToWaterSectionRegexp = Regex("^fertilizer-to-water map:$")
        val waterToLightSectionRegexp = Regex("^water-to-light map:$")
        val lightToTemperatureSectionRegexp = Regex("^light-to-temperature map:$")
        val temperatureToHumiditySectionRegexp = Regex("^temperature-to-humidity map:$")
        val humidityToLocationSectionRegexp = Regex("^humidity-to-location map:$")


        lateinit var seedNumbers : Sequence<Long>
        lateinit var seedRanges : Sequence<LongRange>
        val seedToSoilRangeResolvers = mutableSetOf<SourceToDestinationRangeResolver>()
        val soilToFertiliserRangeResolvers = mutableSetOf<SourceToDestinationRangeResolver>()
        val fertiliserToWaterRangeResolvers = mutableSetOf<SourceToDestinationRangeResolver>()
        val waterToLightRangeResolvers = mutableSetOf<SourceToDestinationRangeResolver>()
        val lightToTemperatureRangeResolvers = mutableSetOf<SourceToDestinationRangeResolver>()
        val temperatureToHumidityRangeResolvers = mutableSetOf<SourceToDestinationRangeResolver>()
        val humidityToLocationRangeResolvers = mutableSetOf<SourceToDestinationRangeResolver>()

        val NUMBER_MATCHER = Regex("[0-9]+")

        private fun createSourceToDestinationRangeResolver(
            specLine : String,
            resolverCollection: MutableSet<SourceToDestinationRangeResolver>
        ) {
            val numbers = NUMBER_MATCHER.findAll(specLine).map { it.value.toLong() }.toList()
            resolverCollection.add(SourceToDestinationRangeResolver(numbers[0], numbers[1], numbers[2]))
        }

        fun fromSpec(spec : String, seedsAsRanges : Boolean) : LocationResolver {
            var currentSection: String? = null
            spec.lines()
                .map { it.trim() }
                .forEach { specLine ->
                    when {
                        seedsSectionRegexp.matches(specLine) -> {
                            if(seedsAsRanges) {
                                seedNumbers = emptySequence()
                                seedRanges = NUMBER_MATCHER.findAll(specLine)
                                        .map { it.value.toLong() }
                                        .chunked(2)
                                        .map {
                                            it.first()..< it.first() + it.last()
                                        }
                            }
                            else {
                                seedNumbers = NUMBER_MATCHER.findAll(specLine).map { it.value.toLong() }
                                seedRanges = emptySequence()
                            }
                            print("Seeds identified: $seedNumbers")
                            return@forEach
                        }

                        seedsToSoilSectionRegexp.matches(specLine) -> {
                            currentSection = "seedsToSoil"
                            return@forEach
                        }

                        soilToFertiliserSectionRegexp.matches(specLine) -> {
                            currentSection = "soilToFertiliser"
                            return@forEach
                        }

                        fertiliserToWaterSectionRegexp.matches(specLine) -> {
                            currentSection = "fertiliserToWater"
                            return@forEach
                        }

                        waterToLightSectionRegexp.matches(specLine) -> {
                            currentSection = "waterToLight"
                            return@forEach
                        }

                        lightToTemperatureSectionRegexp.matches(specLine) -> {
                            currentSection = "lightToTemperature"
                            return@forEach
                        }

                        temperatureToHumiditySectionRegexp.matches(specLine) -> {
                            currentSection = "temperatureToHumidity"
                            return@forEach
                        }

                        humidityToLocationSectionRegexp.matches(specLine) -> {
                            currentSection = "humidityToLocation"
                            return@forEach
                        }

                        specLine == "" -> {
                            currentSection = null
                            return@forEach
                        }
                    }

                    when (currentSection) {
                        "seedsToSoil" -> {
                            createSourceToDestinationRangeResolver(
                                specLine,
                                seedToSoilRangeResolvers
                            )
                        }

                        "soilToFertiliser" -> {
                            createSourceToDestinationRangeResolver(
                                specLine,
                                soilToFertiliserRangeResolvers
                            )
                        }

                        "fertiliserToWater" -> {
                            createSourceToDestinationRangeResolver(
                                specLine,
                                fertiliserToWaterRangeResolvers
                            )
                        }

                        "waterToLight" -> {
                            createSourceToDestinationRangeResolver(
                                specLine,
                                waterToLightRangeResolvers
                            )
                        }

                        "lightToTemperature" -> {
                            createSourceToDestinationRangeResolver(
                                specLine,
                                lightToTemperatureRangeResolvers
                            )
                        }

                        "temperatureToHumidity" -> {
                            createSourceToDestinationRangeResolver(
                                specLine,
                                temperatureToHumidityRangeResolvers
                            )
                        }

                        "humidityToLocation" -> {
                            createSourceToDestinationRangeResolver(
                                specLine,
                                humidityToLocationRangeResolvers
                            )
                        }
                    }
                }
            return LocationResolver(
                seedNumbers,
                seedRanges,
                SourceToDestinationResolver("Seed", "Soil", seedToSoilRangeResolvers),
                SourceToDestinationResolver("Soil", "Fertiliser", soilToFertiliserRangeResolvers),
                SourceToDestinationResolver("Fertiliser", "Water", fertiliserToWaterRangeResolvers),
                SourceToDestinationResolver("Water", "Light", waterToLightRangeResolvers),
                SourceToDestinationResolver("Light", "Temperature", lightToTemperatureRangeResolvers),
                SourceToDestinationResolver("Temperature", "Humidity", temperatureToHumidityRangeResolvers),
                SourceToDestinationResolver("Humidity", "Location", humidityToLocationRangeResolvers),
            )
        }
    }
}


class SourceToDestinationResolver(
    private val sourceType : String,
    private val destinationType : String,
    private val rangeResolvers: Set<SourceToDestinationRangeResolver>
) {
    fun resolve(source: Long) : Long {
        //println("$sourceType to $destinationType resolver:")
        return try {
            rangeResolvers.firstNotNullOf { it.resolve(source) }
        } catch (_ : NoSuchElementException) {
            //println("\t No range resolver found for source '${source}'. Using source value.")
            source
        }
    }
}

data class SourceToDestinationRangeResolver(
    private val destRangeStart: Long,
    private val sourceRangeStart: Long,
    private val rangeLength: Long
) {
    private val destRange = destRangeStart..< destRangeStart + rangeLength
    private val sourceRange = sourceRangeStart..< sourceRangeStart + rangeLength

    fun resolve(source: Long) : Long? {
        return if(sourceRange.contains(source)) {
            val destination = destRange.first + (source - sourceRange.first)
            //println("\t Source '$source' resolves to destination '${destination}")
            destination
        } else {
            null
        }
    }

}