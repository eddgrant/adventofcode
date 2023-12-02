package com.eddgrant.adventofcode.twentythree

enum class Digit(val intValue: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9);

    companion object {
        fun fromInt(soughtIntValue: Int) : Digit? {
            return entries.find { it.intValue == soughtIntValue }
        }

        fun fromString(stringValue : String): Digit {
            return try {
                valueOf(stringValue.uppercase())
            } catch (iae: IllegalArgumentException) {
                fromInt(stringValue.toInt())!!
            }
        }
    }

}