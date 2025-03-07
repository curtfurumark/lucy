package se.curtrune.lucy.modules

import android.app.Application
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import se.curtrune.lucy.classes.google_calendar.GoogleCalendar
import se.curtrune.lucy.classes.google_calendar.GoogleCalendarEvent
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class CalendarModule(private val application: Application) {
    init{
        println("CalendarModule.init()")
        val cr = application.contentResolver
        if( cr != null){
            println("got cr")
        }else{
            println("cr is null")
        }
    }
    companion object{
        const val PROJECTION_ID_INDEX:Int = 0
        const val PROJECTION_ACCOUNT_NAME_INDEX:Int = 1
        const val PROJECTION_DISPLAY_NAME_INDEX:Int = 2
        const val PROJECTION_OWNER_ACCOUNT_INDEX:Int = 3
    }
    private val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,
        CalendarContract.Calendars.ACCOUNT_NAME,
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
        CalendarContract.Calendars.OWNER_ACCOUNT)

    private val PROJECTION_CALENDAR_EVENTS: Array<String> = arrayOf(
        CalendarContract.Events.CALENDAR_ID,
        CalendarContract.Events._ID,
        CalendarContract.Events.TITLE,
        CalendarContract.Events.EVENT_LOCATION,
        CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTEND,
        CalendarContract.Events.ALL_DAY
        //"CalendarContract.Events.CALENDAR_ID"
    )
    private fun cursorToGoogleEvent(cursor: Cursor): GoogleCalendarEvent {
        val calendarID = cursor.getInt(0)
        val eventID = cursor.getInt(1)
        val eventTitle = cursor.getString(2)
        val eventLocation = cursor.getString(3)
        val eventStartMillis = cursor.getLong(4)
        val eventEndMillis = cursor.getLong(5)
        val eventAllDay = cursor.getInt(6)
        val event = GoogleCalendarEvent(
            calendarID = calendarID,
            eventID = eventID,
            accountName = "",
            location =  eventLocation,
            isAllDay = eventAllDay == 1,
            title = eventTitle,
            dtStart = eventStartMillis,
            dtEnd =  eventEndMillis,
            description = ""
        )
        return event
    }

    fun getCalendars(): List<GoogleCalendar> {
        println("getCalendars()")
        val calendarList: MutableList<GoogleCalendar> = mutableListOf()
        val uri = CalendarContract.Calendars.CONTENT_URI
        val selection = ""
        val selectionArgs = emptyArray<String>()
        println("will query")
        val cursor: Cursor?
        try {
            cursor = application.contentResolver.query(
                uri,
                EVENT_PROJECTION,
                selection, selectionArgs,
                null,
            )
        } catch (exception: Exception) {
            println("exception: ${exception.message}")
            exception.printStackTrace()
            return emptyList()
        }
        println("query done")
        if (cursor == null) {
            println("warning cursor is null, returning empty list")
            return calendarList
        }
        while (cursor.moveToNext()) {
            println("cursor moveToNext()....................")
            val id = cursor.getInt(PROJECTION_ID_INDEX)
            val name = cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX)
            val displayName = cursor.getString(PROJECTION_DISPLAY_NAME_INDEX)
            val ownerAccount = cursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX)
            println("id: $id, name: $name, displayName: $displayName, ownerAccount: $ownerAccount")
            val calendar = GoogleCalendar(id, name, displayName, ownerAccount)
            calendarList.add(calendar)
        }
        cursor.close()
        return calendarList
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getEvents(calendarID: Int):List<GoogleCalendarEvent>{
        println("getEvents from calendar with id: $calendarID")
        val events: MutableList<GoogleCalendarEvent> = mutableListOf()
        val bundle = Bundle()
        bundle.putInt(CalendarContract.Events.CALENDAR_ID, calendarID)
        //CalendarContract.Instances.CALENDAR_ID
        //val selectionArgs = arrayOf(calendarID.toString())
        val whereClause = "${CalendarContract.Events.CALENDAR_ID}=$calendarID"
        //val cursor = application.contentResolver.query(CalendarContract.Events.CONTENT_URI, PROJECTION_CALENDAR_EVENTS, bundle, null)
        val cursor = application.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            PROJECTION_CALENDAR_EVENTS,
            CalendarContract.Events.CALENDAR_ID + "= ?", arrayOf( calendarID.toString()), null)
        if( cursor != null){
            val colCount = cursor.columnCount
            println("got myself a cursor, column count: $colCount")
            while(cursor.moveToNext()){
                //println("moving to next/first)")
                events.add(cursorToGoogleEvent(cursor))
            }
        }
        else{
            println("cursor is null")
        }
        cursor?.close()
        return events
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getEvents(): List<GoogleCalendarEvent>{
        println("CalendarModule.getEvents()")
        val events: MutableList<GoogleCalendarEvent> = mutableListOf()
        val cursor = application.contentResolver.query(CalendarContract.Events.CONTENT_URI, null, null, null)

        if( cursor != null) {
            while( cursor.moveToNext()){
                println("........")
                val colIndexCalendarID = cursor.getColumnIndex(CalendarContract.Events.CALENDAR_ID)
                val colIndexEventID = cursor.getColumnIndex(CalendarContract.Events._ID)
                val colIndexAccountName = cursor.getColumnIndex(CalendarContract.Events.ACCOUNT_NAME)
                val colIndexTitle = cursor.getColumnIndex(CalendarContract.Events.TITLE)
                val colIndexDescription = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION)
                val colIndexLocation = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)
                val colIndexStart = cursor.getColumnIndex(CalendarContract.Events.DTSTART)
                val colIndexEnd = cursor.getColumnIndex(CalendarContract.Events.DTEND)
                val colIndexAllDay = cursor.getColumnIndex(CalendarContract.Events.ALL_DAY)
                val calendarID = cursor.getInt(colIndexCalendarID)
                val accountName = cursor.getString(colIndexAccountName)
                val eventTitle = cursor.getString(colIndexTitle)
                val eventLocation = cursor.getString(colIndexLocation)
                val eventDescription = cursor.getString(colIndexDescription)
                val startMillisEpoch = cursor.getLong(colIndexStart)
                val endMillisEpoch = cursor.getLong(colIndexEnd)
                val eventID = cursor.getInt(colIndexEventID)
                val isAllDay = cursor.getInt(colIndexAllDay)
                val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startMillisEpoch), ZoneId.systemDefault())
                //val startDateTime = LocalDateTime.ofEpochSecond((startMillisEpoch.toLong()), 0, ZoneOffset.UTC)
                println("accountName: $accountName, eventTitle: $eventTitle, location: $eventLocation")
                //println("start of event: ${startDateTime.toString()}")
                println("...dateTime: $dateTime")
                println("...is all day $isAllDay")
                events.add(
                    GoogleCalendarEvent(
                        accountName = accountName,
                        title = eventTitle,
                        isAllDay = isAllDay == 1,
                        location = eventLocation,
                        dtStart =  startMillisEpoch,
                        dtEnd = endMillisEpoch,
                        calendarID = calendarID,
                        description = eventDescription,
                        eventID = eventID
                    ))
            }
            cursor.close()
        }else{
            println("cursor is null")
        }
        return events
    }
    fun insertItem(){

    }
    fun insertEvent(myCalendarEvent: GoogleCalendarEvent){
        insertEvent(
            calID =  myCalendarEvent.calendarID.toLong(),
            startMillis = myCalendarEvent.dtStart,
            endMillis = myCalendarEvent.dtEnd,
            title = myCalendarEvent.title,
            description = myCalendarEvent.description,
            timeZone = myCalendarEvent.timeZone)
    }
    private fun insertEvent(calID: Long, startMillis: Long, endMillis : Long, title:String, description: String, timeZone: String){
        println("CalendarModule.insertEvent(...) ")
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.CALENDAR_ID, calID)
            put(CalendarContract.Events.EVENT_TIMEZONE, timeZone)
        }
        val uri: Uri? = application.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        if( uri != null){
            println("...got myself an uri")
        }
        val eventID: Long = uri?.lastPathSegment?.toLong() ?: -1L
        println("...eventID: $eventID")
    }
}