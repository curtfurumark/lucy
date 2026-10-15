package se.curtrune.lucy

import org.junit.Test
import se.curtrune.lucy.util.Converter
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

class ConverterTest {
    @Test
    fun testOne(){
        println("test one")
        val birthDay = LocalDate.of(1963, 10, 31)
        val epoch = birthDay.toEpochSecond(LocalTime.of(0,0), ZoneOffset.ofTotalSeconds(0))
        val epochString = Converter.epochToFormattedDateTime(epoch)
        println(epochString)
        assert(epochString.equals("Thu 31/10 - 1963"))
    }
}