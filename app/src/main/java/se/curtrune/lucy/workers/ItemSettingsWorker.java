package se.curtrune.lucy.workers;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.KeyValueSetting;
import se.curtrune.lucy.classes.Setting;

public class ItemSettingsWorker {
    public static List<Setting> getItemSettings(Item item){
        List<Setting> settings = new ArrayList<>();
        KeyValueSetting settingEvent = new KeyValueSetting(item.getHeading(), item.isAppointment(), Setting.Name.CATEGORY);
        settings.add(settingEvent);

        return settings;
    }
}
