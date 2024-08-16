package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.util.Converter;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>{
    private List<Item> items;
    public static boolean VERBOSE = true;

    public void setList(List<Item> items) {
        if( VERBOSE) log("AppointmentAdapter.setList(List<Item>) size", items.size());
        this.items = items;
        notifyDataSetChanged();
    }

    public void sort() {
        if(VERBOSE)log("AppointmentAdapter.sort()");
        items.sort(Comparator.comparingLong(Item::compare));
        Collections.reverse(items);
        notifyDataSetChanged();
    }

    public interface Callback{
        void onEditTime(Item item);
        void onItemClick(Item item);
        void onLongClick(Item item);
        void onCheckboxClicked(Item item, boolean checked);
    }
    private final Callback callback;

    public AppointmentAdapter(List<Item> items, Callback callback) {
        if( VERBOSE) log("AppointmentAdapter(List<Item>, Callback) items size", items.size());
        this.items = items;
        this.callback = callback;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("appointmentAdapter.onCreateViewHolder(...)");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("AppointmentAdapter.onBindViewHolder() position", position);
        Item item = items.get(position);
        if( VERBOSE) log("...item", item.getHeading());
        holder.textView_heading.setText(item.getHeading());
        holder.textView_info.setText(Converter.format(item.getTargetDate()));
        holder.checkBox_state.setChecked(item.getState().equals(State.DONE));
        holder.textViewTime.setText(Converter.format(item.getTargetTime()));
        holder.setIsRecyclable(false);
        if( item.getColor() != -1 && item.getColor() != 0 ) {
            log("....item", item.getHeading());
            log("...color", item.getColor());
            holder.cardView.setCardBackgroundColor(item.getColor());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void insert(Item item){
        items.add(0, item);
        notifyItemInserted(0);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView_heading;
        private final CheckBox checkBox_state;
        private final TextView textView_info;
        private final TextView textViewTime;
        private final ConstraintLayout layout;
        private final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_heading = itemView.findViewById(R.id.appointmentAdapter_itemHeading);
            textView_info = itemView.findViewById(R.id.appointmentAdapter_itemInfo);
            checkBox_state = itemView.findViewById(R.id.appointmentAdapter_itemState);
            textViewTime = itemView.findViewById(R.id.appointmentAdapter_time);
            cardView = itemView.findViewById(R.id.appointmentAdapter_cardView);
            checkBox_state.setOnClickListener(view -> callback.onCheckboxClicked(items.get(getAdapterPosition()), checkBox_state.isChecked()));
            layout = itemView.findViewById(R.id.appointmentAdapter_mainLayout);
            layout.setOnClickListener(view -> {
                callback.onItemClick(items.get(getAdapterPosition()));
            });
            layout.setOnLongClickListener(e->{
                callback.onLongClick(items.get((getAdapterPosition())));
                return true;
            });
            textViewTime.setOnClickListener(view->{
                callback.onEditTime(items.get(getAdapterPosition()));
            });
        }
    }
}
