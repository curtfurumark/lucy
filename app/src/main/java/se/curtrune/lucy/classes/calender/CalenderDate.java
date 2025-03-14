package se.curtrune.lucy.classes.calender;

import androidx.annotation.NonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.item.Item;

public class CalenderDate {
    private LocalDate date;
    private List<Item> items = new ArrayList<>();

    public LocalDate getDate(){
        return date;
    }
    public int getDay(){
        return date.getDayOfMonth();
    }
    public List<Item> getItems() {
        return items;
    }
    public boolean hasEvents() {
        return items.size() > 0;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setItems(List<Item> items){
        this.items = items;
    }
    public CalenderDate(){

    }
    public CalenderDate(LocalDate date){
        this.date = date;
    }
    public CalenderDate(LocalDate date, Item item){
        this.date = date;
        this.items.add(item);
    }
    public CalenderDate(LocalDate date, List<Item> items){
        this.date = date;
        this.items = items;

    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s, %d events", date.toString(), items.size());
    }
    public DayOfWeek getFirstDayOfMonth(){
        LocalDate firstDate = date.withDayOfMonth(1);
        return firstDate.getDayOfWeek();
    }
    public void addItems(List<Item> items) {
        this.items.addAll(items);
    }

    public void add(Item item) {
        if(items == null){
            items = new ArrayList<>();
        }
        items.add(item);
        //TODO sort items
    }
}
