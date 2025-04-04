package se.curtrune.lucy.util

import android.os.Build
import androidx.annotation.RequiresApi
import se.curtrune.lucy.activities.economy.classes.Transaction
import se.curtrune.lucy.classes.Contact
import se.curtrune.lucy.classes.ItemStatistics
import se.curtrune.lucy.classes.Mental
import se.curtrune.lucy.classes.Notification
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.classes.Type
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.item.Repeat
import se.curtrune.lucy.persist.DB1Result
import se.curtrune.lucy.screens.message_board.Message
import se.curtrune.lucy.web.VersionInfo
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale


open class Logger {
    companion object {
        @JvmStatic
        fun log(calendar: Calendar) {
            log("log(Calendar)")
            log("\tyear", calendar[Calendar.YEAR])
            log(
                "\tmonth",
                calendar[Calendar.MONTH - 1]
            )
            log("\tdate", calendar[Calendar.DATE])
            log(
                "\thour",
                calendar[Calendar.HOUR_OF_DAY]
            )
            log(
                "\tminute",
                calendar[Calendar.MINUTE]
            )
        }

        @JvmStatic
        fun log(result: DB1Result) {
            log("CRBLogger.log(DB1Result)...")
            log("\tid", result.id)
            log("\tphp_file", result.phpFile)
            log("\tis ok", result.isOK)
            log("\tsql", result.sql)
            log("\tjson", result.json)
            log("\tmessage count: ", if (result.messages == null) 0 else result.messages.size)
            if (null != result.messages) {
                for (message in result.messages) {
                    log("\t\tmessage", message)
                }
            }
        }

        @JvmStatic
        fun log(description: String, value: Double) {
            log("$description: $value")
        }

        @JvmStatic
        fun log(item: Item?) {
            log("Logger.log(Item)")
            if (item == null) {
                log("log(Item) called with null item")
                return
            }
            log("\tid", item.id)
            log("\tparent_id ", item.parentId)
            log("\thas child", item.hasChild())
            log("\theading", item.heading)
            log("\tcomment", item.comment)
            log("\ttags", item.tags)
            log("\tdescription", item.description) //TODO, not in use
            log("\tget info", item.info)
            log("\tduration", item.duration)
            log("\tcreated", item.created)
            log("\tupdated", item.updated)
            log("\ttarget_date", item.targetDate)
            log("\ttarget_time", item.targetTime)
            log("\tstate", item.state)
            log("\tcategory", item.category)
            log("\ttype", item.type)
            log("\tis template", item.isTemplate)
            log("\thas estimate", item.hasEstimate())
            log("\t\tisCalenderItem", item.isCalenderItem)
            if (item.hasEstimate()) {
                log("\titem has estimate")
                /*            log("\t\tduration", item.getEstimate().getDuration());
            log("\t\tenergy", item.getEstimate().getEnergy());*/
            }
            log("\thas repeat", item.hasRepeat())
            if (item.hasRepeat()) {
                log("\t\trepeat.toString", item.repeat.toString())
            }
            log("\thas notification", item.hasNotification())
            log("\tcolor", item.color)
            log("\tpriority", item.priority)
            log("\t\tanxiety", item.anxiety)
            log("\t\tenergy", item.energy)
            log("\t\tmood", item.mood)
            log("\t\tstress", item.stress)
            log("\t\trepeat_id", item.repeatID)
        }

        @JvmStatic
        fun log(mental: Mental?) {
            log("log(Mental)")
            if (mental == null) {
                log("log mental called with null mental")
                return
            }
            log("\tid", mental.id)
            log("\titemID", mental.itemID)
            log("\theading", mental.heading)
            log("\tcategory", mental.category)
            log("\tgetInfo()", mental.info)
            log("\tisDone", mental.isDone)
            log("\ttime", mental.time)
            log("\tcomment", mental.comment)
            log("\tdate", mental.date)
            log("\tcreated", Converter.epochToFormattedDateTime(mental.createdEpoch))
            log("\tanxiety", mental.anxiety)
            log("\tmood", mental.mood)
            log("\tenergy", mental.energy)
            log("\tstress", mental.stress)
            log("\tisTemplate", mental.isTemplate)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun log(message: Message) {
            log("Logger.log(Message)")
            log("\tsubject", message.subject)
            log("\tcontent", message.content)
            log("\tuser", message.user)
            log("\tcreated", message.getCreated())
        }

        @JvmStatic
        fun log(notification: Notification) {
            log("log(Notification)")
            log("\tdate", notification.date)
            log("\ttime", notification.time)
            log("\ttype", notification.type.toString())
            log("\ttitle", notification.title)
            log("\tcontent", notification.content)
            log("\ttoJson", notification.toJson())
        }

        @JvmStatic
        fun log(repeat: Repeat?) {
            log("log(Repeat)")
            if (repeat == null) {
                log("...repeat is null, i surrender")
                return
            }
            //Item template = ItemsWorker.selectItem(repeat.getTemplateID(),)
            log("\tid", repeat.id)
            log("\tunit", repeat.unit.toString())
            log("\tqualifier", repeat.qualifier)
            //log("\ttoJson", repeat.toJson());
            log("\ttemplateID", repeat.templateID)
            log("\tfirstDate", repeat.firstDate)
            log("\tlastDate", repeat.lastDate)
            log("\tupdated", repeat.lastActualDate)
            log("\tinfinity", repeat.isInfinite)
        }

        @JvmStatic
        fun log(str: String?) {
            println(str)
        }

        @JvmStatic
        fun log(str: String, e: Exception) {
            log(str + ", " + e.message)
        }

        @JvmStatic
        fun log(description: String, value: Boolean) {
            val message = String.format(Locale.getDefault(), "%s: %b", description, value)
            log(message)
        }

        @JvmStatic
        fun log(description: String, value: Int) {
            log("$description: $value")
        }

        @JvmStatic
        fun log(description: String, value: Float) {
            log(String.format(Locale.getDefault(), "%s: %f", description, value))
        }

        @JvmStatic
        fun log(description: String, value: Long) {
            val message = String.format(Locale.getDefault(), "%s: %d", description, value)
            log(message)
        }

        @JvmStatic
        fun log(description: String, value: String?) {
            log("$description, $value")
        }

        @JvmStatic
        fun log(description: String, time: LocalTime?) {
            val message = if (time == null) {
                String.format(Locale.getDefault(), "%s: null", description)
            } else {
                String.format(Locale.getDefault(), "%s %s", description, time)
            }
            log(message)
        }

        @JvmStatic
        fun log(description: String, value: LocalDate?) {
            val message = if (value == null) {
                String.format(Locale.getDefault(), "%s: null", description)
            } else {
                String.format(Locale.getDefault(), "%s: %s", description, value)
            }
            log(message)
        }

        @JvmStatic
        fun log(description: String, value: LocalDateTime?) {
            log(description, value?.toString() ?: "")
        }

        @JvmStatic
        fun log(description: String, state: State?) {
            if (state == null) {
                log(description, "null")
                return
            }
            log(description, state.toString())
        }

        @JvmStatic
        fun log(description: String, type: Type?) {
            if (type == null) {
                log(description, "null")
                return
            }
            log(description, type.toString())
        }

        @JvmStatic
        fun log(itemStatistics: ItemStatistics?) {
            log("log(ItemStatistics)")
            if (itemStatistics == null) {
                log("WARNING itemStatistics is null")
                return
            }
            log("\tnumber of items", itemStatistics.numberOfItems)
            log("\ttotal duration", Converter.formatSecondsWithHours(itemStatistics.duration))
            log("\taverage energy", itemStatistics.averageEnergy)
            log("\ttotal energy", itemStatistics.energy)
            log("\ttotal anxiety", itemStatistics.anxiety)
            log("\taverage anxiety", itemStatistics.averageAnxiety)
            log("\ttotal stress", itemStatistics.stress)
            log("\ttotal mood", itemStatistics.mood)
        }

        @JvmStatic
        fun log(transaction: Transaction) {
            log("log(Transaction)...")
            log("\tid", transaction.id)
            log("\tgoods", transaction.heading)
            log("\tamount", transaction.amount)
            log("\taccount", transaction.account)
            log("\tdate", transaction.date)
            log("\ttype", transaction.type.toString())
            log("\taccount ordinal", transaction.accountOrdinal)
            println()
        }

        @JvmStatic
        fun log(url: URL) {
            log("log(URL)")
            log("...getHost", url.host)
            log("...getPort", url.port)
            log("...getProtocol", url.protocol)
            log("...getQuery", url.query)
            log("...getUserInfo", url.userInfo)
            log("...getAuthority", url.authority)
            log("...toExternalForm", url.toExternalForm())
        }

        @JvmStatic
        fun log(contact: Contact?) {
            log("log(Contact)")
            if (contact == null) {
                log("cannot log a null contact")
                return
            }
            log("\tdisplayName", contact.displayName)
            log("\tphoneNumber", contact.phoneNumber)
            log("\temail", contact.email)
            log("\tid", contact.id)
        }

        @JvmStatic
        fun log(versionInfo: VersionInfo) {
            log("log(VersionInfo)")
            log("\t\turl", versionInfo.url)
            log("\t\tversionCode", versionInfo.versionCode)
            log("\t\tversionName", versionInfo.versionName)
            log("\t\tinfo", versionInfo.versionInfo)
        }

        @JvmStatic
        fun log(week: Week) {
            log("Logger.log(Week)")
            log("\tweek number", week.weekNumber)
            log("\tfirstDate", week.firstDateOfWeek)
            log("\tlastDate", week.lastDateOfWeek)
            log("\tmonth", week.month.toString())
            log("\tyear", week.year)
        }
    }
}
