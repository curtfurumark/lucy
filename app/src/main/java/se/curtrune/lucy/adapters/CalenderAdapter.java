package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.util.Converter;


public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.ViewHolder>{
    private List<Item> items;
    public static boolean VERBOSE = false;

    public void setList(List<Item> items) {
        if( VERBOSE) log("ItemAdapter.setList(List<Item>) size", items.size());
        this.items = items;
        notifyDataSetChanged();
    }

    public void sort() {
        if(VERBOSE)log("CalenderAdapter.sort()");
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
    private Callback callback;

    public CalenderAdapter(List<Item> items, Callback callback) {
        if( VERBOSE) log("CalenderAdapter(List<Item>, Callback) items size", items.size());
        this.items = items;
        this.callback = callback;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("CalenderAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("CalenderAdapter.onBindViewHolder() position", position);
        Item item = items.get(position);
        holder.textView_heading.setText(item.getHeading());
        holder.textView_info.setText(item.getInfo());
        holder.checkBox_state.setChecked(item.getState().equals(State.DONE));
        holder.textViewTime.setText(Converter.format(item.getTargetTime()));
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

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textView_heading = itemView.findViewById(R.id.calenderAdapter_itemHeading);
            textView_info = itemView.findViewById(R.id.calenderAdapter_itemInfo);
            checkBox_state = itemView.findViewById(R.id.calenderAdapter_itemState);
            textViewTime = itemView.findViewById(R.id.calenderAdapter_time);
            checkBox_state.setOnClickListener(view -> {
                Item item = items.get(getAdapterPosition());
                callback.onCheckboxClicked(items.get(getAdapterPosition()), checkBox_state.isChecked());});
            layout = itemView.findViewById(R.id.calenderAdapter_mainLayout);
            layout.setOnClickListener(view -> {
                log("...layout onClick");
                callback.onItemClick(items.get(getAdapterPosition()));
            });
            layout.setOnLongClickListener(e->{
                log("...onLongClick");
                callback.onLongClick(items.get((getAdapterPosition())));
                return true;
            });
            textViewTime.setOnClickListener(view->{
                callback.onEditTime(items.get(getAdapterPosition()));
            });
        }
    }
}
