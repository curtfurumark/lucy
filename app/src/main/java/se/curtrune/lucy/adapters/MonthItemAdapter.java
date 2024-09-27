package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;

public class MonthItemAdapter extends RecyclerView.Adapter<MonthItemAdapter.ViewHolder> {
    private List<Item> items;
    private LocalDate date;
    public interface Listener{
        void onDateClick(LocalDate date);
    }
    private Listener listener;

    public MonthItemAdapter(List<Item> items, LocalDate date, Listener listener) {
        log("MonthItemAdapter(List<Item>, LocalDate)");
        System.out.printf("date %s, number of items %d\n", date.toString(), items.size());
        this.items = items;
        this.date = date;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.minimal_month_item_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        log("MonthItemAdapter.onBindViewHolder(ViewHolder, int)", position);
        Item item = items.get(position);
        log("...item", item.getHeading());
        holder.textViewHeading.setText(item.getHeading());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setListener(Listener listener){
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewHeading;
        private final ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHeading = itemView.findViewById(R.id.minimalMonthItemAdapter_heading);
            layout = itemView.findViewById(R.id.minimalMonthAdapter_constraintLayout);
            layout.setOnClickListener(view-> {
                log("MonthItemAdapter...onClick!");
                listener.onDateClick(date);
            });
        }
    }
}
