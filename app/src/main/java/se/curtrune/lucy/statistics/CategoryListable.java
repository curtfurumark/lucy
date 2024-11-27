package se.curtrune.lucy.statistics;

import static se.curtrune.lucy.util.Logger.log;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.util.Converter;

public class CategoryListable implements Listable {
    private String category;
    private List<Item> items;

    public CategoryListable(String category, List<Item> categoryItems) {
        this.category = category;
        this.items = categoryItems;
    }

    public String getCategory(){
        return this.category;
    }
    public long getDuration(){
        return items.stream().mapToLong(Item::getDuration).sum();
    }
    public long getEnergy(){
        return items.stream().mapToLong(Item::getDuration).sum();
    }
    @Override
    public String getHeading() {
        return category;
    }

    @Override
    public String getInfo() {
        return String.format("duration %s",
                Converter.formatSecondsWithHours(StatisticsCalculator.getDuration(items)));
    }

    @Override
    public boolean contains(String str) {
        return false;
    }

    @Override
    public long compare() {
        return getDuration() * -1;
    }

    public List<Item> getItems(){
        return items;
    }
    public List<Listable> getListableItems() {
        return new ArrayList<>(items);
    }
}
