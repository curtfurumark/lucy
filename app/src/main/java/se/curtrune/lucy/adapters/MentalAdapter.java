package se.curtrune.lucy.adapters;


import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.util.Converter;


public class MentalAdapter extends RecyclerView.Adapter<MentalAdapter.MyViewHolder>{
    private List<se.curtrune.lucy.classes.Mental> items;
    public boolean VERBOSE = false;
    public enum Mental {
        MOOD, ENERGY, ANXIETY, STRESS
    }
    private Mental mode = Mental.ENERGY;
    public interface Callback{
        void onItemClick(se.curtrune.lucy.classes.Mental item);
    }
    private final Callback callback;

    public MentalAdapter(List<se.curtrune.lucy.classes.Mental> items, Callback callback) {
        assert items != null;
        if( VERBOSE) log("MentalAdapter(List<Mental>, Context, Callback");
        this.items = items;
        this.callback = callback;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("MentalAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mental_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull final MyViewHolder holder, int position) {
        if( VERBOSE) log("MentalAdapter.onBindViewHolder() mode", mode.toString());
        final se.curtrune.lucy.classes.Mental item = items.get(position);
        //log(item);
        switch (mode){
            case MOOD:
                holder.seekBar.setProgress(item.getMood() + Constants.MOOD_OFFSET);
                break;
            case ENERGY:
                holder.seekBar.setProgress(item.getEnergy() +Constants.ENERGY_OFFSET);
                break;
            case ANXIETY:
                holder.seekBar.setProgress(item.getAnxiety() + Constants.ANXIETY_OFFSET);
                break;
            case STRESS:
                holder.seekBar.setProgress(item.getStress() + Constants.STRESS_OFFSET);
                break;
        }
        holder.textViewHeading.setText(item.getHeading());
        //holder.textViewLabel.setText(item.getLabel());
        holder.textViewLabel.setText(Converter.format(item.getTime()));
    }


    @Override
    public int getItemCount() {
        return items != null? items.size(): 0;
    }
    public List<se.curtrune.lucy.classes.Mental> getItems(){
        return items;
    }

    public void setList(List<se.curtrune.lucy.classes.Mental> items) {
        log("MentalAdapter.setList(List<Mental>) size",  items.size());
        if( items == null){
            log("ItemsAdapter.setList(List<Item>) called with null items");
            return;
        }
        this.items = items;
        notifyDataSetChanged();
    }
    public void show(Mental mode){
        log("MentalAdapter.show(Mental)", mode.toString());
        this.mode = mode;
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final SeekBar seekBar;
        private TextView textViewLabel;
        private TextView textViewHeading;


        public MyViewHolder(@androidx.annotation.NonNull android.view.View itemView) {
            super(itemView);

            textViewHeading = itemView.findViewById(R.id.mentalAdapter_heading);
            textViewLabel = itemView.findViewById(R.id.mentalAdapter_labelSeekbar);
            seekBar = itemView.findViewById(R.id.mentalAdapter_seekbar);
            seekBar.setOnTouchListener((view, motionEvent) -> true);//disable seekbar
            ConstraintLayout parentLayout = itemView.findViewById(R.id.mentalAdapter_layout);
            parentLayout.setOnClickListener(view -> {
                if( callback != null){
                    callback.onItemClick(items.get(getAdapterPosition()));
                }
            });
        }
    }

}
