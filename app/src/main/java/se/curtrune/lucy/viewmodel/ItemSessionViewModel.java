package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.item_settings.CheckBoxSetting;
import se.curtrune.lucy.item_settings.ItemSetting;
import se.curtrune.lucy.item_settings.KeyValueSetting;
import se.curtrune.lucy.workers.ItemsWorker;

public class ItemSessionViewModel extends ViewModel {
    private MutableLiveData<Item> mutableItem;
    private ItemSetting currentItemSetting;
    private MutableLiveData<String> mutableError = new MutableLiveData<>();
    private MutableLiveData<Item> mutableCurrentItem = new MutableLiveData<>();
    private static final int MENTAL_OFFSET = 5;
    private Item currentItem;
    public void addTags(String tags, Context context) {
        currentItem.setTags(tags);
        update(currentItem, context);
    }
    public void init(Item item){
        log("ItemSessionViewModel.init(Item)");
        assert  item != null;
        this.currentItem = item;
        this.mutableCurrentItem.setValue(currentItem);

    }
    public LiveData<Item> getCurrentItem(){
        return mutableCurrentItem;
    }
    public LiveData<String> getError(){
        return mutableError;
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
    public List<ItemSetting> getItemSettings(Item item, Context context) {
        assert item != null;
        log("ItemEditorViewModel.getItemSettings(Item)");
        List<ItemSetting> settings = new ArrayList<>();
        KeyValueSetting dateSetting = new KeyValueSetting(context.getString(R.string.date), item.getTargetDate().toString(), ItemSetting.Key.DATE);
        KeyValueSetting timeSetting = new KeyValueSetting(context.getString(R.string.time),item.getTargetTime().toString(), ItemSetting.Key.TIME);
        String repeatValue = "";
        if( item.hasPeriod()){
                repeatValue = item.getPeriod().toString();
        }
        KeyValueSetting repeatSetting = new KeyValueSetting(context.getString(R.string.repeat), repeatValue, ItemSetting.Key.REPEAT);
        KeyValueSetting colourSetting = new KeyValueSetting(context.getString(R.string.color), String.valueOf(item.getColor()), ItemSetting.Key.COLOR);
        CheckBoxSetting templateSetting = new CheckBoxSetting(context.getString(R.string.is_template), item.isTemplate(), ItemSetting.Key.TEMPLATE);
        CheckBoxSetting prioritizedSetting = new CheckBoxSetting( context.getString(R.string.is_prioritized), item.isPrioritized(), ItemSetting.Key.PRIORITIZED);
  /*      String notificationValue = "";
        if( item.hasNotification()){
            notificationValue = item.getNotification().toString();
        }*/
        //KeyValueSetting notificationSetting = new KeyValueSetting("notification", notificationValue, ItemSetting.Key.NOTIFICATION);
        //CheckBoxSetting stateSetting = new CheckBoxSetting("done", item.isDone(), ItemSetting.Key.DONE);
        KeyValueSetting tagsSetting = new KeyValueSetting(context.getString(R.string.tags), item.getTags(), ItemSetting.Key.TAGS);
        CheckBoxSetting calenderSetting = new CheckBoxSetting(context.getString(R.string.is_calender), item.isCalenderItem(), ItemSetting.Key.IS_CALENDAR_ITEM);
        KeyValueSetting categorySetting = new KeyValueSetting(context.getString(R.string.category), item.getCategory(), ItemSetting.Key.CATEGORY);

        settings.add(dateSetting);
        settings.add(timeSetting);
        settings.add(repeatSetting);
        settings.add(categorySetting);
        settings.add(calenderSetting);
        //settings.add(notificationSetting);
        settings.add(prioritizedSetting);
        settings.add(templateSetting);
        settings.add(tagsSetting);
        //settings.add(stateSetting);
        settings.add(colourSetting);
        return settings;
    }
    public boolean update(Item item, Context context){
        log("ItemSessionViewMode.update(Item)", item.getHeading());
        int rowsAffected = ItemsWorker.update(item, context);
        if( rowsAffected != 1){
            mutableError.setValue("ERROR, updating item");
        }else{
            ItemsWorker.touchParents(item, context);
        }
        return rowsAffected == 1;
    }
    public void update(ItemSetting itemSetting, Item item, Context context){
        switch (itemSetting.getKey()) {
            case DATE:
                itemSetting.setValue(item.getTargetDate().toString());
                break;
        }
    }

    public void updateAnxiety(int anxiety, Context context){
        log("...updateAnxiety(int)", anxiety);
        currentItem.setAnxiety(anxiety - MENTAL_OFFSET);
        update(currentItem, context);

    }

    /**
     * updates currentItems energy field and saves it
     * @param energy 0 - 20
     * @param context
     */
    public void updateEnergy(int  energy, Context context) {
        log("ItemSessionViewModel.updateEnergy(int)", energy);
        currentItem.setEnergy(energy - MENTAL_OFFSET);
        update(currentItem, context);
    }

    /**
     *
     * @param mood 0 - 10
     * @param context
     */
    public void updateMood(int mood, Context context){
        log("...updateMood()");
        currentItem.setMood(mood - MENTAL_OFFSET);
        update(currentItem, context);
    }

    /**
     *
     * @param stress, 0 - 10
     * @param context, context
     */
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

    public void setDone(boolean checked) {
        //currentItem.setDone();
    }

    public Mental getMental() {
        log("ItemSessionViewModel.getMental()");
        return currentItem.getMental();
    }

    public void setIsTemplate(boolean isTemplate, Context context) {
        log("ItemSessionViewModel.setIsTemplate(boolean, Context)");
        currentItem.setIsTemplate(isTemplate);
        update(currentItem, context);
    }


}
