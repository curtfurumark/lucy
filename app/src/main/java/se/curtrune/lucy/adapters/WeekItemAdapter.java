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
import se.curtrune.lucy.classes.item.Item;

public class WeekItemAdapter extends RecyclerView.Adapter<WeekItemAdapter.ViewHolder> {
    private List<Item> items;
    private LocalDate date;
    public static boolean VERBOSE = false;
    public interface Listener{
        void onDateClick(LocalDate date);
    }
    private Listener listener;

    public WeekItemAdapter(List<Item> items, LocalDate date) {
        if( VERBOSE) log("WeekItemAdapter(List<Item>, LocalDate)");
        if(VERBOSE) System.out.printf("\tdate %s, number of items %d\n", date.toString(), items.size());
        this.items = items;
        this.date = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.minimal_item_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("WeekItemAdapter.onBindViewHolder(ViewHolder, int) position", position);
        Item item = items.get(position);
        holder.textViewHeading.setText(item.getHeading());
        holder.textViewTime.setText(item.getTargetTime().toString());
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
        private final TextView textViewTime;
        private final ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHeading = itemView.findViewById(R.id.minimalItemAdapter_heading);
            textViewTime = itemView.findViewById(R.id.minimalItemAdapter_time);
            layout = itemView.findViewById(R.id.minimalItemAdapter_constraintLayout);
            layout.setOnClickListener(view->listener.onDateClick(date));
        }
    }
}
