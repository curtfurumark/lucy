package se.curtrune.lucy.classes.calender

import se.curtrune.lucy.classes.item.Item
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

class CalenderDate {
    var date: LocalDate? = null
    private var items: MutableList<Item>? = ArrayList()

    val day: Int
        get() = date!!.dayOfMonth

    fun getItems(): List<Item>? {
        return items
    }

    fun hasEvents(): Boolean {
        return !items!!.isEmpty()
    }

    fun setItems(items: MutableList<Item>?) {
        this.items = items
    }

    constructor()
    constructor(date: LocalDate?) {
        this.date = date
    }

    constructor(date: LocalDate?, item: Item) {
        this.date = date
        items!!.add(item)
    }

    constructor(date: LocalDate?, items: MutableList<Item>?) {
        this.date = date
        this.items = items
    }

    override fun toString(): String {
        return String.format(Locale.getDefault(), "%s, %d events", date.toString(), items!!.size)
    }

    val firstDayOfMonth: DayOfWeek
        get() {
            val firstDate = date!!.withDayOfMonth(1)
            return firstDate.dayOfWeek
        }

    fun addItems(items: List<Item>) {
        this.items!!.addAll(items)
    }

    fun add(item: Item) {
        if (items == null) {
            items = ArrayList()
        }
        items!!.add(item)
        //TODO sort items
    }
}
