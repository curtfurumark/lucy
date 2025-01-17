package se.curtrune.lucy.screens.daycalendar;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.dialogs.PostponeDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.web.CheckForUpdateThread;
import se.curtrune.lucy.web.VersionInfo;
import se.curtrune.lucy.persist.CalenderWorker;
import se.curtrune.lucy.persist.ItemsWorker;

public class CalendarDateViewModel extends ViewModel {
    private MutableLiveData<List<Item>> mutableItems =  new MutableLiveData<>();
    private MutableLiveData<String> mutableError = new MutableLiveData<>();
    private List<Item> items;
    private Item currentParent;
    private LocalDate date;
    public static boolean VERBOSE = false;

    public void add(Item item, Context context) {
        log("CalendarDateViewModel.add(Item, Context)", item.getHeading());
        item = ItemsWorker.insert(item, context);
        if(VERBOSE)log(item);
        items.add(item);
        sort();
        mutableItems.setValue(items);
    }
    public static void checkForUpdate(Context context){
        log("...checkForUpdate()");
        CheckForUpdateThread thread = new CheckForUpdateThread(new CheckForUpdateThread.Callback() {
            @Override
            public void onRequestComplete(VersionInfo versionInfo, boolean res) {
                log("...onRequestComplete(VersionInfo, boolean)");
                log(versionInfo);
                //openUrl(versionInfo.getUrl(), context);
                PackageInfo packageInfo = Lucinda.getPackageInfo(context);
                if( packageInfo != null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if( packageInfo.getLongVersionCode() < versionInfo.getVersionCode()){
                            //log("update lucinda please");
                            openUrl(Constants.DOWNLOAD_LUCINDA_URL, context);
                        }
                    }else{
                        if( packageInfo.versionCode < versionInfo.getVersionCode()){
                            //log("update lucinda please");
                            openUrl(Constants.DOWNLOAD_LUCINDA_URL, context);
                        }
                    }
                }
            }
        });
        thread.start();
    }
    public static void openUrl(String url, Context context){
        log("UpdaterTest.openUrl(String)", url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public boolean delete(Item item, Context context){
        log("CalendarDateViewModel.delete(Item, Context)", item.getHeading());
        boolean stat = true;
        if( item.hasChild()){
            log("...will delete tree");
            ItemsWorker.deleteTree(item, context);
        }else {
            stat = ItemsWorker.delete(item, context);
        }
        if(!stat){
            log("ERROR deleting item", item.getHeading());
            mutableError.setValue("ERROR, deleting item " + item.getHeading());
            return false;
        }
        items.remove(item);
        mutableItems.setValue(items);
        return true;
    }
    public void filter(String filter) {
        log("CalendarDateViewModel.filter(String)");
        mutableItems.setValue(items.stream().filter(item->item.contains(filter)).collect(Collectors.toList()));
    }

    public Item getCurrentParent() {
        return currentParent;
    }
    public int getIndex(Item item) {
        log("CalendarDateViewModel.getIndex(Item)", item.getHeading());
        //return items.indexOf(item); objects not equal...
        for(int index = 0; index < items.size(); index++){
            if( items.get(index).getID() == item.getID()){
                return index;
            }
        }
        return -1;
    }
    public Item getItem(int index) {
        log("CalendarDateViewModel.getItem(int)", index);
        return mutableItems.getValue().get(index);
    }
    public LiveData<List<Item>> getItems(){
        return mutableItems;
    }
    public void selectGenerated(Item parent, Context context) {
        List<Item> items = ItemsWorker.selectTemplateChildren(parent, context);
        mutableItems.setValue(items);
    }
    public int getNextTimePosition(LocalTime time) {
        log("CalendarDateViewModel.getNextTimePosition(LocalTime) ", time.toString());
        int position = 0;
        //DO NOT SCROLL IF ALL ITEMS ARE SHOWN, 8 IS A COMPLETELY RANDOM NUMBER, BY THE WAY
        //TODO
        if(items.size() < 8){
            return 0;
        }
        Item item =items.get(position);
        while(item.getTargetTime().isBefore(time)){
            item = items.get(++position);
        }
        return position -1;
    }
    public void set(LocalDate date, Context context) {
        log("CalendarDateViewModel.set(LocalDate, Context)");
        this.date = date;
        items = ItemsWorker.selectItems(date, context);
/*        if( date.equals(LocalDate.now())) {
            items = ItemsWorker.selectTodayList(date, context);
        }else{
            items = ItemsWorker.selectItems(date, context);
        }*/
        sort();
        mutableItems.setValue(items);
    }
    public void setParent(Item parent, Context context) {
        log("CalendarDateViewModel.setParent(Item, Context)");
        this.currentParent = parent;
        items = ItemsWorker.selectChildren(parent, context);
        items.sort(Comparator.comparingLong(Item::compareTargetDate).thenComparing(Item::compareTargetTime));
        mutableItems.setValue(items);
    }
    public Item getParent(Item child, Context context){
        log("...getParent(Item, Context)");
        return ItemsWorker.getParent(child, context);
    }
    public void goToParent(long id, Context context){
        log("CalendarDateViewModel.goToParent(long, Context)", id);
        Item parent = ItemsWorker.selectItem(id, context);
        log(parent);
    }
    public void moveOnUp(long id, Context context){
        log("CalendarDateViewModel.moveOnUp(long, Context)", id);
        currentParent = ItemsWorker.selectItem(currentParent.getParentId(), context);
        items = ItemsWorker.selectChildren(currentParent, context);
        mutableItems.setValue(items);
    }
    public void sort(){
        log("CalendarDateViewModel.sort()");
        items.sort(Comparator.comparingLong(Item::compareTargetTime));
    }

    public void set(CalenderDate calenderDate) {
        log("CalendarDateViewModel.set(CalendarDate)");
        this.date = calenderDate.getDate();
        this.items = calenderDate.getItems();
        mutableItems.setValue(items);
    }
    public void setEnergyItems(){
        log("CalendarDateViewModel.setEnergyItems()");
        List<Item> colouredItems = new ArrayList<>();
        int currentEnergy = 0;
        for(int i = 0; i < items.size(); i++){
            Item item = items.get(i);
            currentEnergy += item.getEnergy();
            item.setColor(CalenderWorker.getEnergyColor(currentEnergy));
            colouredItems.add(item);
        }
        mutableItems.setValue(colouredItems);
    }
    public void postpone(Item item, PostponeDialog.Postpone postpone, LocalDate date, Context context) {
        switch (postpone){
            case ONE_HOUR:
                LocalTime targetTime = item.getTargetTime();
                item.setTargetTime(targetTime.plusHours(1));
                items.sort(Comparator.comparingLong(Item::compareTargetTime));
                break;
            case ONE_DAY:
                item.setTargetDate(date.plusDays(1));
                items.remove(item);
                break;
            case ONE_WEEK:
                item.setTargetDate(date.plusWeeks(1));
                items.remove(item);
                break;
            case ONE_MONTH:
                item.setTargetDate(date.plusMonths(1));
                items.remove(item);
                break;
        }
        update(item, context);
    }


    public boolean update(Item item, Context context){
        log("CalendarDateViewModel.update(Item)", item.getHeading());
        int rowsAffected = ItemsWorker.update(item, context);
        if( rowsAffected != 1){
            log("ERROR updating item");
            return false;
        }
        sort();
        mutableItems.setValue(items);
        return true;
    }
}
