package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import se.curtrune.lucy.R;

public class CalenderViewHolder extends RecyclerView.ViewHolder {
    public final TextView textViewDayOfMonth;
    public static boolean VERBOSE = false;
    private final MonthAdapter.OnItemListener listener;
    public CalenderViewHolder(@NonNull View itemView, MonthAdapter.OnItemListener listener) {
        super(itemView);
        textViewDayOfMonth = itemView.findViewById(R.id.calender_cell_day);
        this.listener = listener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log("onClick(View)");
            }
        });
        //itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition(), textViewDayOfMonth.getText().toString()));

    }


}
