package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.workers.ItemsWorker;


public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.ViewHolder>{
    private List<Item> items;
    public static boolean VERBOSE = false;

    public void setList(List<Item> items) {
        if( VERBOSE) log("SequenceAdapter.setList(List<Item>) size", items.size());
        this.items = items;
        notifyDataSetChanged();
    }

    public void sort() {
        if(VERBOSE)log("SequenceAdapter.sort()");
        items.sort(Comparator.comparingLong(Item::compare).reversed());
        notifyDataSetChanged();
    }

    public interface Callback{
        void onItemClick(Item item);
        void onEditTime(Item item);
        void onEditDuration(Item item);
        void onLongClick(Item item);
        void onCheckboxClicked(Item item, boolean checked);
    }
    private Callback callback;

    public SequenceAdapter(List<Item> items, Callback callback) {
        if( VERBOSE) log("SequenceAdapter(List<Item>, Callback) items size", items.size());
        this.items = items;
        this.callback = callback;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("SequenceAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sequence_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( VERBOSE) log("SequenceAdapter.onBindViewHolder() position", position);
        Item item = items.get(position);
        holder.textView_heading.setText(item.getHeading());
        holder.textView_info.setText(item.getInfo());
        holder.checkBox_state.setChecked(item.getState().equals(State.DONE));
        holder.textViewTargetTime.setText(Converter.format(item.getTargetTime()));
        String txtEstimatedDuration = String.format(Locale.getDefault(), "estimated duration %s", Converter.formatSecondsWithHours(item.getEstimatedDuration()));
        holder.textViewEstimatedDuration.setText(txtEstimatedDuration);
        String textEstimatedEnergy = String.format(Locale.getDefault(), "estimated energy %d", item.getEnergy());
        holder.textViewEstimatedEnergy.setText(textEstimatedEnergy);
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
        private final TextView textViewTargetTime;
        private final TextView textViewEstimatedDuration;
        private final TextView textViewEstimatedEnergy;
        private final TextView textViewEdit;
        private final ImageView imageViewClock;

        public ViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            textView_heading = itemView.findViewById(R.id.sequenceAdapter_itemHeading);
            textView_info = itemView.findViewById(R.id.sequenceAdapter_itemInfo);
            checkBox_state = itemView.findViewById(R.id.sequenceAdapter_itemState);
            textViewTargetTime = itemView.findViewById(R.id.sequenceAdapter_targetTime);
            textViewEstimatedDuration = itemView.findViewById(R.id.sequenceAdapter_estimatedDuration);
            textViewEstimatedEnergy = itemView.findViewById(R.id.sequenceAdapter_estimatedEnergy);
            textViewEdit = itemView.findViewById(R.id.sequenceAdapter_edit);
            imageViewClock  = itemView.findViewById(R.id.sequenceAdapter_clock);
            checkBox_state.setOnClickListener(view -> {
                callback.onCheckboxClicked(items.get(getAdapterPosition()), checkBox_state.isChecked());
            });
            textViewEdit.setOnClickListener(view->callback.onItemClick(items.get(getAdapterPosition())));
            textViewTargetTime.setOnClickListener(view->callback.onEditTime(items.get(getAdapterPosition())));
            textViewEstimatedDuration.setOnClickListener(view->callback.onEditDuration(items.get(getAdapterPosition())));
        }

    }
}
