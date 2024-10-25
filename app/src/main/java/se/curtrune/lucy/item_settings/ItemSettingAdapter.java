package se.curtrune.lucy.item_settings;


import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;


public class ItemSettingAdapter extends RecyclerView.Adapter {

    private List<ItemSetting> settings;
    public static boolean VERBOSE = false;
    public interface Listener{
        void onClick(ItemSetting setting);
    }
    private Listener listener;
    public ItemSettingAdapter(List<ItemSetting> items, Listener listener) {
        this.listener = listener;
        this.settings = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(VERBOSE)log("...onCreateViewHolder(ViewGroup, int) viewType", viewType);
        switch (viewType){
            case 0:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.key_value_holder, parent, false);
                return new StringHolder(itemView);
            default:
                View itemView2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_holder, parent, false);
                return new CheckBoxHolder(itemView2);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if( VERBOSE) log("ItemSettingAdapter.onBindViewHolder(...) position", position);
            ItemSetting setting = settings.get(position);
            if(setting instanceof KeyValueSetting){
                //((StringHolder)holder).heading.setText(setting.getHeading());
                ((StringHolder)holder).set((KeyValueSetting) setting);
            }else if( setting instanceof  CheckBoxSetting){
                ((CheckBoxHolder) holder).checkBox.setChecked(((CheckBoxSetting) setting).isChecked());
                ((CheckBoxHolder)holder).checkBox.setText(setting.getHeading());
            }
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    @Override
    public int getItemViewType(int position) {
        ItemSetting item = settings.get(position);
        return item.getViewType().ordinal();
    }

    public class StringHolder extends RecyclerView.ViewHolder{
        public TextView heading;
        public TextView value;
        private CardView cardView;

        public StringHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.stringHolder_heading);
            cardView = itemView.findViewById(R.id.stringHolder_cardView);
            value = itemView.findViewById(R.id.stringHolder_value);
            cardView.setOnClickListener(view->{
                log("...onStringSetting click cardview");
                ItemSetting setting = settings.get(getAdapterPosition());
                listener.onClick(settings.get(getAdapterPosition()));
            });
        }
        public void set(KeyValueSetting setting){
            if(VERBOSE) log("StringHolder.set(KeyValueSetting)", setting.toString());
            heading.setText(setting.getHeading());
            value.setText(setting.getValue());
        }
    }
    public class CheckBoxHolder extends RecyclerView.ViewHolder{
        public CheckBox checkBox;
        private CardView cardView;
        public CheckBoxHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxLayout_checkBox);
            cardView = itemView.findViewById(R.id.checkboxHolder_cardView);
            checkBox.setOnClickListener(view->{
                log("...onCheckBox click");
                if( settings.get(getAdapterPosition()) instanceof  CheckBoxSetting){
                    CheckBoxSetting setting = (CheckBoxSetting) settings.get(getAdapterPosition());
                    setting.setChecked(checkBox.isChecked());
                    listener.onClick(settings.get(getAdapterPosition()));
                }else {
                    log("...not instanceof checkBox");
                }
            });
        }
    }
}
