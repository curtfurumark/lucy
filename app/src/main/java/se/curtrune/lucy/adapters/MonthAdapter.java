package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.calender.CalenderDate;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ViewHolder> {
    public interface OnDateListener{
        void onDateClick(CalenderDate calenderDate);
    }
    private OnDateListener listener;
    private List<CalenderDate> calenderDates;
    public static boolean VERBOSE = false;

    public MonthAdapter(List<CalenderDate> calenderDates, OnDateListener listener) {
        log("MonthAdapter(List<String>, OnItemListener), size", calenderDates.size());
        this.calenderDates = calenderDates;
        this.listener = listener;
    }
/*    public MonthAdapter(List<String> daysOfMonth, OnItemListener listener) {
        log("MonthAdapter(List<String>, OnItemListener), size", daysOfMonth.size());
        this.daysOfMonth = daysOfMonth;
        this.listener = listener;
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calender_cell, parent, false);
        //ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        //layoutParams.height = (int)(parent.getHeight() * 0.1666666);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("...onBindViewHolder(CalenderViewHolder, int) position", position);
        CalenderDate calenderDate = calenderDates.get(position);
        int dayOfMonth = calenderDate.getDay();
        holder.textViewDayOfMonth.setText(String.valueOf(dayOfMonth));
        if( calenderDate.hasEvents()) {
            holder.textViewDayOfMonth.setTextColor(Color.RED);
        }
        holder.textViewDayOfMonth.setText(String.valueOf(dayOfMonth));
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
        private TextView textViewDayOfMonth;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDayOfMonth = itemView.findViewById(R.id.calender_cell_day);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    log("onClick(View)");
                    listener.onDateClick(calenderDates.get(getAdapterPosition()));
                }
            });
            //itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), textViewDayOfMonth.getText().toString()));
        }
    }
}
