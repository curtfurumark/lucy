package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.calender.Week;


public class CalenderDateAdapter extends RecyclerView.Adapter<CalenderDateAdapter.ViewHolder>{
    private List<LocalDate> dates;
    private Week week;
    public static boolean VERBOSE = false;

    public void setList(Week week) {
        if( VERBOSE) log("CalenderDateAdapter.setList(List<Item>) size", dates.size());
        this.dates = week.getDates();
        this.week = week;
        notifyDataSetChanged();
    }


    public interface Callback{
        void onDateSelected(LocalDate date);
    }
    private Callback callback;

    public CalenderDateAdapter(Week week, Callback callback) {
        if( VERBOSE) log("CalenderDateAdapter(Week, Callback)");
        this.dates = week.getDates();
        this.week = week;
        this.callback = callback;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("CalenderDateAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_date_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("CalenderDateAdapter.onBindViewHolder() position", position);
        LocalDate date = dates.get(position);
        if( date.equals(week.getCurrentDate())){
            //log("...selectedDate true", date.toString());
            holder.textViewDateNumber.setTextColor(Color.rgb(160,32,240));
            holder.textViewDateName.setTextColor(Color.rgb(160,32,240));
        }else{
            holder.textViewDateNumber.setTextColor(Color.rgb(128,128,128));
            holder.textViewDateName.setTextColor(Color.rgb(128,128,128));
        }
        String dateName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        holder.textViewDateName.setText(dateName);
        holder.textViewDateNumber.setText(String.valueOf(date.getDayOfMonth()));
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewDateName;
        private final TextView textViewDateNumber;
        private final ConstraintLayout layout;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textViewDateName = itemView.findViewById(R.id.calender_date_adapter_dateName);
            textViewDateNumber = itemView.findViewById(R.id.calender_date_adapter_dateNumber);
            layout = itemView.findViewById(R.id.calenderDateFragment_rootLayout);
            layout.setOnClickListener(view->{
                log("...layout onClick");
                //selectedDate = dates.get(getAdapterPosition());
                callback.onDateSelected(dates.get(getAdapterPosition()));
            });
        }
    }
}
