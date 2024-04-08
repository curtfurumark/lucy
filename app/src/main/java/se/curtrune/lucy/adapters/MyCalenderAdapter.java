package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;

public class MyCalenderAdapter extends RecyclerView.Adapter<CalenderViewHolder> {
    public interface OnItemListener{
        void onItemClick(int position, String dayText);
    }
    private OnItemListener listener;
    private final List<String> daysOfMonth;

    public MyCalenderAdapter(List<String> daysOfMonth, OnItemListener listener) {
        log("MyCalenderAdapter(List<String>, OnItemListener), size", daysOfMonth.size());
        this.daysOfMonth = daysOfMonth;
        this.listener = listener;
    }

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
        log("...onBindViewHolder(CalenderViewHolder, int) position", position);
        log("\t\tdayOfMonth", daysOfMonth.get(position));
        holder.textViewDayOfMonth.setText(daysOfMonth.get(position));
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }
}
