package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.calender.DateHourCell;
import se.curtrune.lucy.classes.calender.Week;


public class DateHourAdapter extends RecyclerView.Adapter<DateHourAdapter.ViewHolder>{
    private List<DateHourCell> dateHourCells;
    private int hour;
    private Week week;
    public static boolean VERBOSE = true;

    public void setList(List<DateHourCell> dateHourCells) {
        if( VERBOSE) log("DateHourAdapter.setList(List<DateHourCell>)");
        this.dateHourCells = dateHourCells;
        notifyDataSetChanged();
    }

    public interface Callback{
        void onDateHourCellSelected(DateHourCell dateHourCell);
    }
    private Callback callback;

    public DateHourAdapter(List<DateHourCell> dateHourCells,  Callback callback) {
        if( VERBOSE) log("DateHourAdapter(Week, Callback)");
        this.dateHourCells = dateHourCells;
        this.callback = callback;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("DateHourAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_hour_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("DateHourAdapter.onBindViewHolder() position", position);
        DateHourCell dateHourCell = dateHourCells.get(position);
        holder.textViewHourLabel.setText(String.valueOf(dateHourCell.getHour()));
    }

    @Override
    public int getItemCount() {
        return dateHourCells.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewHourLabel;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textViewHourLabel = itemView.findViewById(R.id.hourAdapter_hour);
        }
    }
}
