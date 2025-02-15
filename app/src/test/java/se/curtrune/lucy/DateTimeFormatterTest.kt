package se.curtrune.lucy

import org.junit.Test
import se.curtrune.lucy.util.DateTImeFormatter

class DateTimeFormatterTest {

    @Test
    fun testOneDay(){
        println("testOneDay()")
        var seconds = 24 * 3600 // one day
        seconds += 3600          //plus one hour
        seconds += 60            //plus one minuter
        seconds += 42            //plus 42 seconds
        //expect 1 day 01:01:42
        val formattedString = DateTImeFormatter.formatSeconds(seconds.toLong())
        println("formattedString: $formattedString")
        assert(formattedString.equals("1 day 01:01:42"))
    }
    @Test
    fun testManyDays() {
        val seconds = 24 * 3600 * 24
        val formattedString = DateTImeFormatter.formatSeconds(seconds.toLong())
        println("formattedString $formattedString")
        assert(formattedString == "24 days 00:00:00")
    }

    @Test
    fun testSeconds(){
        println("testSeconds")
        val seconds: Long = 42
        val formattedString = DateTImeFormatter.formatSeconds(seconds)
        println("formattedString $formattedString")
        assert(formattedString == "00:00:42")
    }
}