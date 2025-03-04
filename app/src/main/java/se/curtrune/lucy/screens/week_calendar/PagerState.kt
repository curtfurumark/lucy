package se.curtrune.lucy.screens.week_calendar

data class PagerState(
    val numPages: Int = 100,
    val initialPage: Int = 50,
    var currentPage: Int = 50
)
