package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.calender.DateHourCell;
import se.curtrune.lucy.classes.calender.Week;


public class DateHourAdapter extends RecyclerView.Adapter<DateHourAdapter.ViewHolder>{
    private List<DateHourCell> dateHourCells;
    public static boolean VERBOSE = true;

    public interface Callback{
        void onClick(DateHourCell dateHourCell);
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
        log("...type", dateHourCell.getType().toString());
        DateHourCell.Type type = dateHourCell.getType();
        holder.setIsRecyclable(false);
        switch (type){
            case EMPTY_CELL:
                break;
            case TIME_CELL:
                LocalTime time = dateHourCell.getTime();
                //holder.textViewHourLabel.setText(String.valueOf(dateHourCell.getHour()));
                holder.textViewHourLabel.setText(time.toString());
                holder.textViewDate.setVisibility(View.GONE);
                break;
            case EVENT_CELL:
                holder.textViewHourLabel.setVisibility(View.GONE);
                if( dateHourCell.hasEvents()){
                    String strEvents = String.format(Locale.getDefault(), "%d", dateHourCell.getEvents().size());
                    holder.textViewDate.setText(strEvents);

                }else {
                    holder.textViewDate.setText("X");
                }
                break;
            case DATE_CELL:
                LocalDate date = dateHourCell.getDate();
                holder.textViewHourLabel.setText(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
                holder.textViewDate.setText(String.valueOf(date.getDayOfMonth()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dateHourCells.size();
    }
    public void setList(List<DateHourCell> dateHourCells) {
        if( VERBOSE) log("DateHourAdapter.setList(List<DateHourCell>)");
        this.dateHourCells = dateHourCells;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewHourLabel;
        private final TextView textViewDate;
        private final LinearLayout layout;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textViewHourLabel = itemView.findViewById(R.id.hourAdapter_hour);
            textViewDate = itemView.findViewById(R.id.hourAdapter_date);
            layout = itemView.findViewById(R.id.hourAdapter_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(dateHourCells.get(getAdapterPosition()));
                }
            });
        }
    }
}
