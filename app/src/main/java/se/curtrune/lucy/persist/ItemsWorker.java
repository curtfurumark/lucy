package se.curtrune.lucy.persist;

import static se.curtrune.lucy.app.Settings.Root.APPOINTMENTS;
import static se.curtrune.lucy.app.Settings.Root.DAILY;
import static se.curtrune.lucy.app.Settings.Root.PANIC;
import static se.curtrune.lucy.app.Settings.Root.PROJECTS;
import static se.curtrune.lucy.app.Settings.Root.THE_ROOT;
import static se.curtrune.lucy.app.Settings.Root.TODO;
import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.classes.item.Repeat;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.workers.NotificationsWorker;

public class ItemsWorker {
    public static boolean VERBOSE = false;


}
