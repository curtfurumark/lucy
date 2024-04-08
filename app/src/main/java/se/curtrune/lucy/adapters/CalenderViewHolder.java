package se.curtrune.lucy.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import se.curtrune.lucy.R;

public class CalenderViewHolder extends RecyclerView.ViewHolder {
    public final TextView textViewDayOfMonth;
    private final MyCalenderAdapter.OnItemListener listener;
    public CalenderViewHolder(@NonNull View itemView, MyCalenderAdapter.OnItemListener listener) {
        super(itemView);
        textViewDayOfMonth = itemView.findViewById(R.id.calender_cell_day);
        this.listener = listener;
        itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), textViewDayOfMonth.getText().toString()));

    }


}
