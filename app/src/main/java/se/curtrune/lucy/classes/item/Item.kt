package se.curtrune.lucy.classes.item

import com.google.gson.Gson
import kotlinx.serialization.Serializable
import se.curtrune.lucy.classes.Contact
import se.curtrune.lucy.classes.Content
import se.curtrune.lucy.classes.ItemDuration
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.classes.Reward
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.features.notifications.Notification
import se.curtrune.lucy.util.Converter
import se.curtrune.lucy.util.Logger.Companion.log
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.Locale


@Serializable
class Item()  {
    var id: Long = 0 //iD?
    var parentId: Long = 0
    var repeatID: Long = 0
    var anxiety: Int = 0
    var category: String
    var children: MutableList<Item>?
    var comment: String = ""
    var contact: Contact? = null
    var content: Content? = null
    var color: Int = -1

    var createdEpoch: Long = 0

    var description: String = ""
    //@JvmField
    var duration: Long = 0
    var energy: Int = 0
    protected var has_child: Int = 0

    var heading: String = ""
    var isCalenderItem: Boolean = false
    var isSyncWithGoogle: Boolean = false
    var itemDuration: ItemDuration? = null
    var mood: Int = 0


    var parent: Item? = null
    var priority: Int = 0


    @JvmField
    var repeat: Repeat? = null
    var tags: String = ""
    protected var templateType: Int = 0

    var targetDateEpochDay: Long = 0
        protected set
    var targetTimeSecondOfDay: Int = 0
    var type: Int =Type.NODE.ordinal
    var state: Int = State.TODO.ordinal
    var stress: Int = 0
    var updatedEpoch: Long = 0
        protected set


    //protected MentalStats estimate;
    @JvmField
    var notification: Notification? = null

    //return mental != null? mental.getEnergy():0;
    var reward: Reward? = null

    /**
     * 0 = not a template in anyway
     * templateRoot, has templateChildren
     * templateGenerated...
     */

    init {
        this.updatedEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        this.createdEpoch = this.updatedEpoch
        description = tags
        comment = description
        heading = comment
        category = heading
        state = State.TODO.ordinal
        type = Type.NODE.ordinal
        has_child = 0
        children = emptyList<Item>().toMutableList()
    }

    constructor(heading: String) : this() {
        this.heading = heading
    }

    /**
     * when a template spawns a child
     * @param other, the template item
     */
    constructor(other: Item) : this() {
        this.setType(other.getType())
        this.heading = other.heading
        this.duration = other.duration
        this.category = other.category
        this.tags = other.tags
        this.targetTimeSecondOfDay = LocalTime.now().toSecondOfDay()
        this.targetDateEpochDay = LocalDate.now().toEpochDay()
        //this.estimate = item.getEstimate();
        this.repeatID = other.repeatID
        this.color = other.color
    }

    fun compare(): Long {
        if (type == Type.APPOINTMENT.ordinal) {
            return this.targetDateEpochDay
        }
        return (if (state == State.DONE.ordinal) Long.Companion.MIN_VALUE + this.updatedEpoch else this.updatedEpoch) * -1
    }

    fun compareTargetTime(): Long {
        return targetTimeSecondOfDay.toLong()
    }

    fun contains(str: String): Boolean {
        return (heading + description + comment + tags + category).contains(str)
    }


    fun getCreated(): LocalDateTime {
        return LocalDateTime.ofEpochSecond(this.createdEpoch, 0, ZoneOffset.UTC)
    }

    fun getInfo(): String {
        val temp = Type.APPOINTMENT.ordinal
        if (type == temp) {
            return String.format(
                "%s %s", Converter.format(
                    Converter.epochToDate(
                        this.targetDateEpochDay
                    )
                ), Converter.epochTimeToFormattedString(this.targetTimeSecondOfDay)
            )
        }
        return String.format(Locale.getDefault(), "%s", this.targetDate.toString())
    }



    var mental: Mental
        get() = Mental(anxiety, energy, mood, stress)
        set(mental) {
            energy = mental.energy
            mood = mental.mood
            anxiety = mental.anxiety
            stress = mental.stress
        }

    fun getState(): State {
        return State.entries[state]
    }


    var targetDate: LocalDate
        get() = LocalDate.ofEpochDay(this.targetDateEpochDay)
        set(localDate) {
            this.targetDateEpochDay = localDate.toEpochDay()
        }
    var targetTime: LocalTime
        get() = LocalTime.ofSecondOfDay(targetTimeSecondOfDay.toLong())
        set(value) {
            targetTimeSecondOfDay = value.toSecondOfDay()
        }

    fun getType(): Type {
        return Type.entries[type]
    }

    fun getUpdated(): LocalDateTime? {
        return LocalDateTime.ofEpochSecond(this.updatedEpoch, 0, ZoneOffset.UTC)
    }


    fun hasChild(): Boolean {
        return has_child == 1
    }

    fun hasColor(): Boolean {
        return color != -1 && color != 0
    }

    fun hasEstimate(): Boolean {
        //return estimate != null;
        return false
    }

    fun hasItemParent(): Boolean {
        return parent != null
    }

    fun hasRepeat(): Boolean {
        return repeat != null
    }

    fun hasNotification(): Boolean {
        return notification != null
    }

    fun hasReward(): Boolean {
        return reward != null
    }

    fun hasTags(): Boolean {
        return !this.tags.isEmpty()
    }

    fun isCategory(category: String?): Boolean {
        return this.category.equals(category, ignoreCase = true)
    }

    /**
     * checks if this item has targetTime
     * @param date the date to check
     * @return true if date equals this items targetDate
     */
    fun isDate(date: LocalDate): Boolean {
        return this.targetDateEpochDay == date.toEpochDay()
    }

    fun isDateHour(date: LocalDate, time: LocalTime): Boolean {
        return isDate(date) && isTargetTimeHour(time)
    }

    var isDone: Boolean
        get() = state == State.DONE.ordinal
        set(isDone) {
            this.state =
                if (isDone) State.DONE.ordinal else State.TODO.ordinal
        }

    val isPrioritized: Boolean
        get() = priority == 1


    fun isState(state: State): Boolean {
        return this.state == state.ordinal
    }

    fun isTargetTimeHour(time: LocalTime): Boolean {
        return this.targetTime.getHour() == time.getHour()
    }

    var isTemplate: Boolean = false


    fun isUpdated(date: LocalDate): Boolean {
        val localDateTime = date.atStartOfDay()
        val epoch = localDateTime.toEpochSecond(ZoneOffset.UTC)
        return this.updatedEpoch >= epoch && this.updatedEpoch <= epoch + (3600 * 24)
    }

    fun setCreated(created: LocalDateTime) {
        this.updatedEpoch = created.toEpochSecond(ZoneOffset.UTC)
    }

    fun setCreated(created: Long) {
        this.createdEpoch = created
    }

/*    fun setId(id: Long) {
        this.id = id
    }*/

    fun setHasChild(hasChild: Boolean) {
        this.has_child = if (hasChild) 1 else 0
    }
    fun setReward(json: String?) {
        this.reward = Gson().fromJson<Reward?>(json, Reward::class.java)
    }
    fun setIsDone(isDone: Boolean) {
        this.state = if (isDone) State.DONE.ordinal else State.TODO.ordinal
    }

    fun setState(state: State) {
        this.state = state.ordinal
    }

    fun setTargetDate(target_date: Long) {
        this.targetDateEpochDay = target_date
    }

/*    fun setTargetTime(target_time: Int) {
        this.targetTimeSecondOfDay = target_time
    }*/

    fun setType(type: Type) {
        this.type = type.ordinal
    }


/*    fun setTargetTime(localTime: LocalTime?) {
        this.targetTimeSecondOfDay = if (localTime != null) localTime.toSecondOfDay() else 0
    }*/

    fun setUpdated(updated: LocalDateTime) {
        this.updatedEpoch = updated.toEpochSecond(ZoneOffset.UTC)
    }

    fun setUpdated(updated: Long) {
        this.updatedEpoch = updated
    }

    override fun toString(): String {
        return String.format(
            Locale.getDefault(),
            "%s, time: %s, date: :%s",
            this.heading,
            this.targetTime.toString(),
            this.targetDate.toString()
        )
        //return String.format(Locale.getDefault(), "%s, %s,  d:%s", getHeading(), getTargetTime().toString(), Converter.formatSecondsWithHours(duration));
    }

    /**
     * sets the next targetDate,
     * to be called when item is done
     */
    @Deprecated(message = "not used as of the time being")
    fun updateTargetDate() {
        log("...updateTargetDate()")
        if (hasRepeat()) {
            //TODO, repeater.get next date?
            //target_date = repeat.getNextDate().toEpochDay();
        }
    }

    fun getID(): Long {
        return id
    }

    var isAppointment: Boolean
        get() = type == Type.APPOINTMENT.ordinal
        set(isAppointment) {
            this.type =
                if (isAppointment) Type.APPOINTMENT.ordinal else Type.NODE.ordinal
        }


    companion object {
        //protected MediaContent content;
        var VERBOSE: Boolean = false
    }
}
