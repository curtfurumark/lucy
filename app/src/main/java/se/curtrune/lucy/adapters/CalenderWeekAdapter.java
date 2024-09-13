package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.util.Converter;


public class CalenderWeekAdapter extends RecyclerView.Adapter<CalenderWeekAdapter.ParentHolder> {

    private List<CalenderDate> calenderDates;
    private final RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
    public interface Listener{
        void onDateClick(LocalDate date);
    }
    private Listener listener;
    public CalenderWeekAdapter(List<CalenderDate> calenderDates, Listener listener) {
        this.calenderDates = calenderDates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_adapter_layout, parent, false);
        return new ParentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentHolder holder, int position) {
        CalenderDate dateItem =  calenderDates.get(position);
        holder.textViewDate.setText(dateItem.getDate().format(DateTimeFormatter.ofPattern("E d")));
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.childRecycler.getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.childRecycler.getContext());
        linearLayoutManager.setInitialPrefetchItemCount(dateItem.getItems().size());
        dateItem.getItems().sort(Comparator.comparingLong(Item::compareTargetTime));
        MinimalItemAdapter minimalItemAdapter = new MinimalItemAdapter(dateItem.getItems(), dateItem.getDate());
        minimalItemAdapter.setListener(new MinimalItemAdapter.Listener() {
            @Override
            public void onDateClick(LocalDate date) {
                log("...MinimalAdapter.onDateClick(LocalDate)", date.toString());
            }
        });
        holder.childRecycler.setAdapter(minimalItemAdapter);
        holder.childRecycler.setLayoutManager(linearLayoutManager);
        holder.childRecycler.setRecycledViewPool( recycledViewPool);
        //holder.layout.setOnClickListener(view->listener.onDateClick(dateItem.getDate()));
    }

    @Override
    public int getItemCount() {
        return calenderDates.size();
    }
    public void setCalenderDates(List<CalenderDate> calenderDates){
        this.calenderDates = calenderDates;
        notifyDataSetChanged();

    }

    public class ParentHolder extends RecyclerView.ViewHolder{
        private final TextView textViewDate;
        private final RecyclerView childRecycler;
        private final ConstraintLayout layout;

        public ParentHolder(@NonNull View itemView) {
            super(itemView);
            log("ParentHolder(View)");
            int index = getAdapterPosition();
            log("...index", index);
            //CalenderDate calenderDate = calenderDates.get(getAdapterPosition());
            textViewDate = itemView.findViewById(R.id.parentAdapter_textViewDate);
            childRecycler = itemView.findViewById(R.id.parentAdapter_recycler);
            layout = itemView.findViewById(R.id.calenderWeekAdapter_layout);
            //layout.setOnClickListener(view->listener.onDateClick(calenderDates.get(getAdapterPosition()).getDate()));
            itemView.setOnClickListener(view->listener.onDateClick(calenderDates.get(getAdapterPosition()).getDate()));
        }
    }
}
