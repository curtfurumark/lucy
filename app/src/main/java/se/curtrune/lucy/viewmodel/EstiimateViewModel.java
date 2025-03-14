package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.classes.ItemStatistics;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.persist.ItemsWorker;

public class EstiimateViewModel extends ViewModel {
    private List<Item> items;
    private List<Item> doneItems;
    private ItemStatistics currentStats;
    private ItemStatistics estimatedStats;
    public static boolean VERBOSE = false;
    private MutableLiveData<Mental> mutableMentalEstimate = new MutableLiveData<>();
    private MutableLiveData<Mental> mutableMentalCurrent = new MutableLiveData<>();
    private MutableLiveData<ItemStatistics> mutableCurrentStats = new MutableLiveData<>();
    private MutableLiveData<ItemStatistics> mutableEstimatedStats = new MutableLiveData<>();

    private LocalDate date;
    public void init(LocalDate date, Context context){
        log("EstimateViewModel.init(LocalDate, Context)", date.toString());
        this.date = date;
        items = ItemsWorker.selectItems(date, context);
        if( VERBOSE) items.forEach(System.out::println);
        doneItems = items.stream().filter(item -> item.isDone()).collect(Collectors.toList());
        currentStats = new ItemStatistics(doneItems);
        estimatedStats = new ItemStatistics(items);
        mutableCurrentStats.setValue(currentStats);
        mutableEstimatedStats.setValue(estimatedStats);
    }
    public LiveData<ItemStatistics> getCurrentItemStatics() {
        return mutableCurrentStats;
    }
    public LiveData<ItemStatistics> getEstimatedItemStatistics(){
        return mutableEstimatedStats;
    }
    public LiveData<Mental> getMentalEstimate(){
        return mutableMentalEstimate;
    }
    public LiveData<Mental> getCurrentMental(){
        return mutableMentalCurrent;
    }


}
