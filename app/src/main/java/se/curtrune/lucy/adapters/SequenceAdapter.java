package se.curtrune.lucy.adapters;

import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.util.Converter;


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
        String textEstimatedEnergy = String.format(Locale.getDefault(), "%s %d", holder.textViewEstimatedEnergy.getContext().getString(R.string.energy), item.getEnergy());
        holder.textViewEstimatedEnergy.setText(textEstimatedEnergy);
        String textEstimatedStress = String.format(Locale.getDefault(), " %s %d", holder.textViewEstimatedStress.getContext().getString(R.string.stress),item.getStress());
        holder.textViewEstimatedStress.setText(textEstimatedStress);
        String textEstimatedAnxiety = String.format(Locale.getDefault(), "%s %d",holder.textViewEstimatedAnxiety.getContext().getString(R.string.anxiety) ,item.getAnxiety());
        holder.textViewEstimatedAnxiety.setText(textEstimatedAnxiety);
        String textEstimatedMood = String.format(Locale.getDefault(), "%s %d",holder.textViewEstimatedMood.getContext().getString(R.string.mood) ,item.getMood());
        holder.textViewEstimatedMood.setText(textEstimatedMood);
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
        private final TextView textViewEstimatedAnxiety;
        private final TextView textViewEstimatedStress;
        private final TextView textViewEstimatedMood;

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
            textViewEstimatedStress = itemView.findViewById(R.id.sequenceAdapter_estimatedStress);
            textViewEstimatedAnxiety = itemView.findViewById(R.id.sequenceAdapter_estimatedAnxiety);
            textViewEstimatedMood = itemView.findViewById(R.id.sequenceAdapter_estimatedMood);
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
