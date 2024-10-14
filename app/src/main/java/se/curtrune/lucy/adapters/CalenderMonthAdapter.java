package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;

public class CalenderMonthAdapter extends RecyclerView.Adapter<CalenderMonthAdapter.ViewHolder> {
    private final RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
    public interface OnDateListener{
        void onCalenderDateClick(CalenderDate calenderDate);
    }
    private final OnDateListener listener;
    private List<CalenderDate> calenderDates;
    public static boolean VERBOSE = false;

    public CalenderMonthAdapter(List<CalenderDate> calenderDates, OnDateListener listener) {
        log("CalenderMonthAdapter(List<String>, OnItemListener), size", calenderDates.size());
        this.calenderDates = calenderDates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calender_cell, parent, false);
        //ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        //layoutParams.height = (int)(parent.getHeight() * 0.1666666);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalenderDate dateItem =  calenderDates.get(position);
        holder.textViewDayOfMonth.setText(String.valueOf(dateItem.getDate().getDayOfMonth()));
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.childRecycler.getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setInitialPrefetchItemCount(dateItem.getItems().size());
        dateItem.getItems().sort(Comparator.comparingLong(Item::compareTargetTime));
        MonthItemAdapter minimalItemAdapter = new MonthItemAdapter(dateItem.getItems(), dateItem.getDate(), new MonthItemAdapter.Listener() {
            @Override
            public void onDateClick(LocalDate date) {
                log("...onDateClick(LocalDate)", date.toString());
                CalenderDate calenderDate = getCalenderDate(date);
                if( calenderDate != null) {
                    listener.onCalenderDateClick(calenderDate);
                }else{
                    log("ERROR could not find CalenderDate item for date", date.toString());
                }
            }
        });
        holder.recyclerView.setAdapter(minimalItemAdapter);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setRecycledViewPool( recycledViewPool);
        //holder.layout.setOnClickListener(view->listener.onDateClick(dateItem.getDate()));
    }
    private CalenderDate getCalenderDate(LocalDate date){
        log("...findCalenderDate()");
        for(int i = 0; i < calenderDates.size(); i++){
            if( calenderDates.get(i).getDate().equals(date)){
                return calenderDates.get(i);
            }
        }
        return null;
    }


    public void setList(List<CalenderDate> dates){
        this.calenderDates = dates;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return calenderDates.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        protected final TextView textViewDayOfMonth;
        private final RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDayOfMonth = itemView.findViewById(R.id.calender_cell_day);
            recyclerView = itemView.findViewById(R.id.calender_cell_recycler);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    log("onClick(View)");
                    listener.onCalenderDateClick(calenderDates.get(getAdapterPosition()));
                }
            });
        }
    }
}
