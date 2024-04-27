package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.calender.CalenderDate;

public class MonthAdapter extends RecyclerView.Adapter<CalenderViewHolder> {
    public interface OnItemListener{
        void onItemClick(int position, String dayText);
    }
    public interface OnDateListener{
        void onDateClick(CalenderDate calenderDate);
    }
    private OnItemListener listener;
    private OnDateListener dateListener;
    //private final List<String> daysOfMonth;
    private List<CalenderDate> calenderDates;
    public static boolean VERBOSE = false;

    public MonthAdapter(List<CalenderDate> calenderDates, OnDateListener listener) {
        log("MonthAdapter(List<String>, OnItemListener), size", calenderDates.size());
        //this.daysOfMonth = daysOfMonth;
        this.calenderDates = calenderDates;
        this.dateListener = listener;
    }
/*    public MonthAdapter(List<String> daysOfMonth, OnItemListener listener) {
        log("MonthAdapter(List<String>, OnItemListener), size", daysOfMonth.size());
        this.daysOfMonth = daysOfMonth;
        this.listener = listener;
    }*/

    @NonNull
    @Override
    public CalenderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calender_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        layoutParams.height = (int)(parent.getHeight() * 0.1666666);
        return new CalenderViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalenderViewHolder holder, int position) {
        if( VERBOSE) log("...onBindViewHolder(CalenderViewHolder, int) position", position);
        //if(VERBOSE)log("\t\tdayOfMonth", daysOfMonth.get(position));
        CalenderDate calenderDate = calenderDates.get(position);
        int dayOfMonth = calenderDate.getDay();
        holder.textViewDayOfMonth.setText(String.valueOf(dayOfMonth));
        if( calenderDate.hasEvents()) {
            holder.textViewDayOfMonth.setTextColor(Color.RED);
        }
        holder.textViewDayOfMonth.setText(String.valueOf(dayOfMonth));
    }

    @Override
    public int getItemCount() {
        return calenderDates.size();
    }
}
