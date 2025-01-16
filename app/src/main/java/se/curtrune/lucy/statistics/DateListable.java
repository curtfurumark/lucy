package se.curtrune.lucy.statistics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.screens.util.Converter;

public class DateListable implements Listable {
    private LocalDate date;
    private List<Item> items;

    public DateListable(LocalDate date, List<Item> items) {
        this.date = date;
        this.items = items;
    }

    @Override
    public String getHeading() {
        return date.toString();
    }
    public long getDuration(){
        return items.stream().mapToLong(Item::getDuration).sum();
    }

    @Override
    public String getInfo() {
        return String.format("%s", Converter.formatSecondsWithHours(getDuration()));
    }

    @Override
    public boolean contains(String str) {
        return date.toString().equals(str);
    }

    @Override
    public long compare() {
        return date.toEpochDay() * -1;
    }

    public List<Item> getItems() {
        return items;
    }
    public List<Listable> getListableItems(){
        return new ArrayList<>(items);
    }
}
