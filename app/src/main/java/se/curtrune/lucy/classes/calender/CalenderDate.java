package se.curtrune.lucy.classes.calender;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;

public class CalenderDate {
    private LocalDate date;
    private List<Item> items;

    public LocalDate getDate(){
        return date;
    }
    public int getDay(){
        return date.getDayOfMonth();
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setItems(List<Item> items){
        this.items = items;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s, %d events", date.toString(), items.size());
    }

    public boolean hasEvents() {
        return items.size() > 0;
    }
}