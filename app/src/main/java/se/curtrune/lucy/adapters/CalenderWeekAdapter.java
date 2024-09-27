package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;


public class CalenderWeekAdapter extends RecyclerView.Adapter<CalenderWeekAdapter.ParentHolder> {

    private List<CalenderDate> calenderDates;
    private final RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
    public interface Listener{
        void onCalenderDateClick(CalenderDate calenderDate);
    }
    private final Listener listener;
    public CalenderWeekAdapter(List<CalenderDate> calenderDates, Listener listener) {
        log("CalenderWeekAdapter(...)");
        this.calenderDates = calenderDates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_week_adapter, parent, false);
        return new ParentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentHolder holder, int position) {
        log("CalenderWeekAdapter.onBindViewHolder(...) position", position);
        CalenderDate dateItem =  calenderDates.get(position);
        holder.textViewDate.setText(dateItem.getDate().format(DateTimeFormatter.ofPattern("E d")));
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.childRecycler.getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.childRecycler.getContext());
        linearLayoutManager.setInitialPrefetchItemCount(dateItem.getItems().size());
        dateItem.getItems().sort(Comparator.comparingLong(Item::compareTargetTime));
        WeekItemAdapter weekItemAdapter = new WeekItemAdapter(dateItem.getItems(), dateItem.getDate());
        weekItemAdapter.setListener(new WeekItemAdapter.Listener() {
            @Override
            public void onDateClick(LocalDate date) {
                log("...MinimalAdapter.onDateClick(LocalDate)", date.toString());
                CalenderDate calenderDate = getCalenderDate(date);
                if( calenderDate != null){
                    listener.onCalenderDateClick(calenderDate);
                }else{
                    log("ERROR, could not find CalenderDate for date", date.toString());
                }
            }
        });
        holder.childRecycler.setAdapter(weekItemAdapter);
        holder.childRecycler.setLayoutManager(linearLayoutManager);
        holder.childRecycler.setRecycledViewPool( recycledViewPool);
/*        holder.layout.setOnClickListener(view->
        {
            log("CalenderWeekAdapter, layout, onclick");
            listener.onCalenderDateClick(calenderDates.get(position));
        });*/
    }

    private CalenderDate getCalenderDate(LocalDate date){
        log("...getCalenderDate(LocalDate)", date.toString());
        for( int i = 0; i < calenderDates.size(); i++){
            if( calenderDates.get(i).getDate().equals(date)){
                return calenderDates.get(i);
            }
        }
        return null;
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
            textViewDate = itemView.findViewById(R.id.calenderWeekAdapter_textViewDate);
            childRecycler = itemView.findViewById(R.id.calenderWeekAdapter_recycler);
            layout = itemView.findViewById(R.id.calenderWeekAdapter_layout);
            layout.setOnClickListener(view->listener.onCalenderDateClick(calenderDates.get(getAdapterPosition())));
            //itemView.setOnClickListener(view->listener.onDateClick(calenderDates.get(getAdapterPosition()).getDate()));
        }
    }
}
