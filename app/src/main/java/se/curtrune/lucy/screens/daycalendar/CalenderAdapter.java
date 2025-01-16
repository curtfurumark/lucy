package se.curtrune.lucy.screens.daycalendar;

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

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.screens.util.Converter;


public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.ViewHolder>{
    private List<Item> items;
    public static boolean VERBOSE = false;

    public void setList(List<Item> items) {
        if( VERBOSE) log("CalenderAdapter.setList(List<Item>) size", items.size());
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
    private final Callback callback;

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
        if( VERBOSE) log("...item", item.getHeading());
        //notifyDataSetChanged();
/*        if (position == items.size() - 1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.bottomMargin = 150; // last item bottom margin
            holder.itemView.setLayoutParams(params);

        } else {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.bottomMargin = 10; // other items bottom margin
            holder.itemView.setLayoutParams(params);
        }*/
        holder.textView_heading.setText(item.getHeading());
        holder.textView_info.setVisibility(View.GONE);
        holder.checkBox_state.setChecked(item.getState().equals(State.DONE));
        String timeDateText;
        if(item.getTargetDate().equals(LocalDate.now())){
            timeDateText = Converter.format(item.getTargetTime());
        }else{
            timeDateText = Converter.formatShort(item.getTargetDate());
        }
        //holder.textViewTime.setText(Converter.format(item.getTargetTime()));
        holder.textViewTime.setText(timeDateText);
        holder.setIsRecyclable(false);
        //if( item.getColor() != -1 && item.getColor() != 0 ) {
        if(item.hasColor()){
            if(VERBOSE) {
                log("....item", item.getHeading());
                log("...color", item.getColor());
            }
            holder.cardView.setCardBackgroundColor(item.getColor());
        }
        if(item.hasChild()){
            holder.textViewHasChild.setVisibility(View.VISIBLE);
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
        private final TextView textViewHasChild;
        private final TextView textView_info;
        private final TextView textViewTime;
        private final ConstraintLayout layout;
        private final CardView cardView;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textView_heading = itemView.findViewById(R.id.calenderAdapter_itemHeading);
            textViewHasChild = itemView.findViewById(R.id.calenderAdapter_textViewHasChildren);
            textView_info = itemView.findViewById(R.id.calenderAdapter_itemInfo);
            checkBox_state = itemView.findViewById(R.id.calenderAdapter_itemState);
            textViewTime = itemView.findViewById(R.id.calenderAdapter_time);
            cardView = itemView.findViewById(R.id.calenderAdapter_cardView);
            checkBox_state.setOnClickListener(view -> callback.onCheckboxClicked(items.get(getAdapterPosition()), checkBox_state.isChecked()));
            layout = itemView.findViewById(R.id.calenderAdapter_mainLayout);
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
