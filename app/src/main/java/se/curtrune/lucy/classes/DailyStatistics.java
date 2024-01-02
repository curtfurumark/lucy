package se.curtrune.lucy.classes;

import java.time.LocalDate;
import java.util.List;

public class DailyStatistics {
    private List<Listable> items;
    private LocalDate date;
    private long seconds;

    private List<Listable>  typeStats;

    public void setDate(LocalDate date){
        this.date = date;
    }

    public void setTypeList(List<Listable> typeStatistics) {
        this.typeStats = typeStatistics;
    }

    public void setItems(List<Listable> items){
        this.items = items;

    }
    public int getTotalSeconds(){
        int sum = 0;
        for(Listable listable: items){
            Item item = (Item) listable;
            sum += item.getDuration();
        }
        return  sum;
    }
}
