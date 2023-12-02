package com.eddgrant.adventofcode

import com.eddgrant.adventofcode.twentytwo.ElfUtils
import javax.xml.crypto.Data

enum class DataType(val filename: String) {
    TEST("test-input.txt"),
    REAL("real-input.txt")
}

class DataProvider {

    companion object {
        fun getData(year: Int, day: Int, part: Int, type : DataType): String {
            return DataProvider::class.java.getResource("/$year/day$day/part$part/${type.filename}")!!.readText(Charsets.UTF_8)
        }
    }

}