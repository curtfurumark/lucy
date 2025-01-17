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
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.item_settings.CheckBoxSetting;
import se.curtrune.lucy.item_settings.ItemSetting;
import se.curtrune.lucy.item_settings.KeyValueSetting;
import se.curtrune.lucy.screens.util.Converter;
import se.curtrune.lucy.util.Kronos;
import se.curtrune.lucy.persist.ItemsWorker;
import se.curtrune.lucy.workers.NotificationsWorker;

public class ItemSessionViewModel extends ViewModel {
    private MutableLiveData<Item> mutableItem;
    private ItemSetting currentItemSetting;
    private final MutableLiveData<Kronos.State> mutableTimerState = new MutableLiveData<>();
    private final MutableLiveData<Long> mutableDuration = new MutableLiveData<>();
    private final MutableLiveData<String> mutableError = new MutableLiveData<>();
    private final MutableLiveData<Item> mutableCurrentItem = new MutableLiveData<>();
    private static final int MENTAL_OFFSET = 5;
    public static boolean VERBOSE = false;
    private Item currentItem;
    private Item currentTimerItem;
    private Kronos kronos;
    public void addTags(String tags, Context context) {
        currentItem.setTags(tags);
        update(currentItem, context);
    }
    public void cancelNotification(Context context) {
        log("ItemSessionViewModel.cancelNotification(Context)");
        NotificationsWorker.cancelNotification(currentItem, context);
        currentItem.setNotification(null);
        update(currentItem, context);
    }
    public void cancelTimer(){
        log("ItemSessionViewModel.cancelTimer()");
        if( kronos == null){
            return;
        }
        kronos.reset();
        mutableDuration.setValue(0L);
        mutableTimerState.setValue(Kronos.State.STOPPED);
    }
    public void init(Item item, Context context){
        log("ItemSessionViewModel.init(Item)");
        assert  item != null;
        this.currentItem = item;
        if(currentItem.getRepeatID() > 0){
            Repeat repeat = ItemsWorker.selectRepeat(currentItem.getRepeatID(), context);
            currentItem.setRepeat( repeat);
        }
        this.mutableCurrentItem.setValue(currentItem);
        this.mutableDuration.setValue(currentItem.getDuration());

    }
    public LiveData<Item> getCurrentItem(){
        return mutableCurrentItem;
    }
    public LiveData<Long> getDuration(){
        return mutableDuration;
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
        KeyValueSetting durationSetting = new KeyValueSetting(context.getString(R.string.duration), Converter.formatSecondsWithHours(item.getDuration()),ItemSetting.Key.DURATION);
        String notificationValue = "";

        if( item.hasNotification()){
            notificationValue = item.getNotification().toString();
        }
        KeyValueSetting notificationSetting = new KeyValueSetting("notification", notificationValue, ItemSetting.Key.NOTIFICATION);
        KeyValueSetting tagsSetting = new KeyValueSetting(context.getString(R.string.tags), item.getTags(), ItemSetting.Key.TAGS);
        CheckBoxSetting calenderSetting = new CheckBoxSetting(context.getString(R.string.is_calender), item.isCalenderItem(), ItemSetting.Key.IS_CALENDAR_ITEM);
        KeyValueSetting categorySetting = new KeyValueSetting(context.getString(R.string.category), item.getCategory(), ItemSetting.Key.CATEGORY);

        settings.add(dateSetting);
        settings.add(timeSetting);
        settings.add(repeatSetting);
        settings.add(categorySetting);
        settings.add(calenderSetting);
        settings.add(notificationSetting);
        settings.add(durationSetting);
        settings.add(prioritizedSetting);
        settings.add(templateSetting);
        settings.add(tagsSetting);
        settings.add(colourSetting);
        return settings;
    }
    public Mental getMental() {
        log("ItemSessionViewModel.getMental()");
        return currentItem.getMental();
    }
    public void pauseTimer(){
        log("ItemSessionViewModel.pauseTimer()");
        if( kronos == null){
            return;
        }
        mutableTimerState.setValue(Kronos.State.PAUSED);
        kronos.pause();
    }
    public void resumeTimer(){
        log("ItemSessionViewModel.resumeTimer()");
        if(kronos == null){
            log("WARNING, trying to resume a null Timer");
            return;
        }
        mutableTimerState.setValue(Kronos.State.RUNNING);
        kronos.resume();
    }
    public void setElapsedDuration(long secs){
        log("ItemSessionViewModel.setElapsedDuration(long)", secs);
        if( kronos != null) {
            kronos.setElapsedTime(secs);
        }
    }
    public void stopTimer(){
        log("ItemSessionViewModel.stopTimer()");
        if( kronos == null){
            log("WARNING, stopTimer() called with null kronos");
        }

        kronos.stop();
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
     * @param energy 0 - 10
     * @param context, context context
     */
    public void updateEnergy(int  energy, Context context) {
        log("ItemSessionViewModel.updateEnergy(int)", energy);
        currentItem.setEnergy(energy - MENTAL_OFFSET);
        update(currentItem, context);
    }

    /**
     *
     * @param mood 0 - 10
     * @param context, context context
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
        mutableDuration.setValue(duration.getSeconds());
        update(currentItem, context);
    }

    public void setDone(boolean checked) {
        //currentItem.setDone();
    }

    public void startTimer(){
        log("ItemSessionViewModel.startTimer()");
        currentTimerItem = currentItem;
        kronos = Kronos.getInstance(secs -> {
            if(VERBOSE )log("Kronos.onTimerTick(long)", secs);
            mutableDuration.setValue(secs);
        });
        mutableTimerState.setValue(Kronos.State.RUNNING);
        kronos.setElapsedTime(currentTimerItem.getDuration());
        kronos.start(currentTimerItem.getID());

    }

    public void setIsTemplate(boolean isTemplate, Context context) {
        log("ItemSessionViewModel.setIsTemplate(boolean, Context)");
        currentItem.setIsTemplate(isTemplate);
        update(currentItem, context);
    }
    public Item getItem() {
        log("ItemSessionViewModel.getItem()");
        return currentItem;
    }

    public boolean itemHasRepeat() {
        log("ItemSessionViewModel.itemHasRepeat(Context)");
        return currentItem.getRepeatID() > 0;
    }

    public void setIsPrioritized(boolean checked, Context context) {
        currentItem.setPriority(checked ? 1:0);
        update(currentItem, context);
    }

    public LiveData<Kronos.State> getTimerState() {
        log("ItemSessionViewModel.getTimerState()");
        if(kronos == null){
            mutableTimerState.setValue(Kronos.State.PENDING);
        }
        return mutableTimerState;
    }


    public void updateNotification(Context context) {
        log("ItemSessionViewModel.updateNotification(Context)");
    }
}
