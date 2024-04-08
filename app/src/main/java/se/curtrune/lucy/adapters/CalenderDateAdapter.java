package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;


public class CalenderDateAdapter extends RecyclerView.Adapter<CalenderDateAdapter.ViewHolder>{
    private List<LocalDate> dates;
    public static boolean VERBOSE = false;

    public void setList(List<LocalDate> dates) {
        if( VERBOSE) log("ItemAdapter.setList(List<Item>) size", dates.size());
        this.dates = dates;
        notifyDataSetChanged();
    }

    public void sort() {
        if(VERBOSE)log("TodayAdapter.sort()");
/*        items.sort(Comparator.comparingLong(Item::compare));
        Collections.reverse(items);
        notifyDataSetChanged();*/
    }

    public interface Callback{
        void onDateSelected(LocalDate date);
    }
    private Callback callback;

    public CalenderDateAdapter(List<LocalDate> dates, Callback callback) {
        if( VERBOSE) log("ItemAdapter(List<Item>, Callback) dates size", dates.size());
        this.dates = dates;
        this.callback = callback;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("ItemAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_date_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("CalenderDateAdapter.onBindViewHolder() position", position);
        LocalDate date = dates.get(position);
        String dateName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
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

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textViewDateName = itemView.findViewById(R.id.calender_date_adapter_dateName);
            textViewDateNumber = itemView.findViewById(R.id.calender_date_adapter_dateNumber);
        }
    }
}
