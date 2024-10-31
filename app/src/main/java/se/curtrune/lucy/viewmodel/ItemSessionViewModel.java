package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.widget.CheckBox;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.item_settings.CheckBoxSetting;
import se.curtrune.lucy.item_settings.ItemSetting;
import se.curtrune.lucy.item_settings.KeyValueSetting;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;

public class ItemSessionViewModel extends ViewModel {
    private MutableLiveData<Item> mutableItem;
    private ItemSetting currentItemSetting;
    private static final int MENTAL_OFFSET = 5;
    private Item currentItem;
    //private Mental currentMental;
    public void init(Item item){
        log("ItemSessionViewModel.init(Item)");
        this.currentItem = item;
        //this.currentMental = item.getMental();
        //assert currentMental != null;
        assert currentItem != null;

    }
    public int getStress() {
        return currentItem.getStress();
    }
    public int getAnxiety(){
        return currentItem.getAnxiety();
    }
    public int getMood(){
        return currentItem.getMood();
    }
    public int getEnergy(){
        return currentItem.getEnergy() ;
    }
    public List<ItemSetting> getItemSettings(Item item) {
        assert item != null;
        log("ItemEditorViewModel.getItemSettings(Item)");
        List<ItemSetting> settings = new ArrayList<>();
        KeyValueSetting dateSetting = new KeyValueSetting("date", item.getTargetDate().toString(), ItemSetting.Key.DATE);
        KeyValueSetting timeSetting = new KeyValueSetting("time", item.getTargetTime().toString(), ItemSetting.Key.TIME);
        String repeatValue = "";
        if( item.hasPeriod()){
                repeatValue = item.getPeriod().toString();
        }
        KeyValueSetting repeatSetting = new KeyValueSetting("repeat", repeatValue, ItemSetting.Key.REPEAT);
        KeyValueSetting colourSetting = new KeyValueSetting("color", String.valueOf(item.getColor()), ItemSetting.Key.COLOR);
        CheckBoxSetting templateSetting = new CheckBoxSetting("template", item.isTemplate(), ItemSetting.Key.TEMPLATE);
        CheckBoxSetting prioritizedSetting = new CheckBoxSetting( "prioritized", item.isPrioritized(), ItemSetting.Key.PRIORITIZED);
        String notificationValue = "";
        if( item.hasNotification()){
            notificationValue = item.getNotification().toString();
        }
        KeyValueSetting notificationSetting = new KeyValueSetting("notification", notificationValue, ItemSetting.Key.NOTIFICATION);
        //CheckBoxSetting appointmentSetting = new CheckBoxSetting("appointment", false, ItemSetting.Key.IS_CALENDAR_ITEM);
        CheckBoxSetting stateSetting = new CheckBoxSetting("done", item.isDone(), ItemSetting.Key.DONE);
        KeyValueSetting tagsSetting = new KeyValueSetting("tags", item.getTags(), ItemSetting.Key.TAGS);
        CheckBoxSetting calenderSetting = new CheckBoxSetting("calendar", item.isCalenderItem(), ItemSetting.Key.IS_CALENDAR_ITEM);
        KeyValueSetting categorySetting = new KeyValueSetting("category", item.getCategory(), ItemSetting.Key.CATEGORY);

        settings.add(dateSetting);
        settings.add(timeSetting);
        settings.add(repeatSetting);
        settings.add(categorySetting);
        settings.add(calenderSetting);
        settings.add(notificationSetting);
        //settings.add(appointmentSetting);
        settings.add(prioritizedSetting);
        settings.add(templateSetting);
        settings.add(tagsSetting);
        settings.add(stateSetting);
        settings.add(colourSetting);
        return settings;
    }
    public boolean update(Item item, Context context){
        log("ItemSessionViewMode.update(Item)", item.getHeading());
        int rowsAffected = ItemsWorker.update(item, context);
        return rowsAffected == 1;
    }
    public void update(ItemSetting itemSetting, Item item, Context context){
        switch (itemSetting.getKey()){
            case DATE:
                itemSetting.setValue(item.getTargetDate().toString());
        }
    }

    public void updateAnxiety(int anxiety, Context context){
        log("...updateAnxiety(int)", anxiety);
        //currentMental.setAnxiety(anxiety - MENTAL_OFFSET);
        //update(currentMental, context);

    }
    public void updateEnergy(int  energy, Context context) {
        log("ItemSessionViewModel.updateEnergy(int)", energy);
        currentItem.setEnergy(energy);
        update(currentItem, context);
    }
    public void updateMood(int mood, Context context){
        log("...updateMood()");
        currentItem.setMood(mood - MENTAL_OFFSET);
        update(currentItem, context);

    }
    public void updateStress(int stress, Context context){
        log("...updateStress(int)", stress);
        currentItem.setStress(stress - MENTAL_OFFSET);
        update(currentItem, context);
    }
    public void setIsEvent(boolean checked, Context context) {
        log("...setIsEvent(boolean()", checked);
        currentItem.setIsCalenderItem(checked);
        update(currentItem, context);
    }
    public void setCategory(String category, Context context) {
        log("...setCategory(String, Context)");
        currentItem.setCategory(category);
        update(currentItem, context);
    }

    public void setTargetTime(LocalTime targetTime, Context context) {
        log("...setTargetTime(LocalTime)", targetTime);
        currentItem.setTargetTime(targetTime);
        update(currentItem, context);
    }

    public void setDuration(Duration duration, Context context) {
        log("...setDuration(Duration, Context)");
        currentItem.setDuration(duration.getSeconds());
        update(currentItem, context);
    }
}
