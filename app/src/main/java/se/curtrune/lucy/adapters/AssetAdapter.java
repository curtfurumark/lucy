package se.curtrune.lucy.adapters;



import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.economy.classes.Asset;
import se.curtrune.lucy.activities.economy.classes.Transaction;


public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.MyViewHolder>{
    private List<Asset> assets;
    public boolean VERBOSE = true;
    public void setList(List<Asset> items) {
        if( items == null){
            log("ERROR,, AssetsAdapter.setList(List<Transaction>) called with null items");
            return;
        }
        if( VERBOSE) log("AssetsAdapter.setList(List<Transaction>), size ", items.size());
        this.assets = items;
        notifyDataSetChanged();
    }

    public interface Callback{
        void onItemClick(Asset asset);
        void onItemLongClick(Asset asset);
        //void onCheckboxClicked(Listable item, boolean checked);
    }
    private final Callback callback;

    public AssetAdapter(List<Asset> items, Callback callback) {
        if( VERBOSE) log("ItemsAdapter(List<Item>, Context, Callback) size", items.size());
        if (items == null){
            log("WARNING, taskList is null");
        }
        this.assets = items;
        this.callback = callback;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("ItemsAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listable_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull final MyViewHolder holder, int position) {
        if( VERBOSE) log("ItemsAdapter.onBindViewHolder();");
        final Asset asset=  assets.get(position);
        //TODO rename textView
        holder.textView_heading.setText(asset.getHeading());
        holder.textView_info.setText(asset.getInfo());
    }


    @Override
    public int getItemCount() {
        return assets.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView_heading;
        private final TextView textView_info;

        public MyViewHolder(@androidx.annotation.NonNull android.view.View itemView) {
            super(itemView);
            textView_heading = itemView.findViewById(R.id.listableAdapter_heading);
            textView_info = itemView.findViewById(R.id.listableAdapter_info);
            //checkBox_state = itemView.findViewById(R.id.checkBox_taskAdapter_state);
            //checkBox_state.setOnClickListener(view -> callback.onCheckboxClicked(items.get(getAdapterPosition()), checkBox_state.isChecked()));
            ConstraintLayout parentLayout = itemView.findViewById(R.id.listableAdapter_rootLayout);
            parentLayout.setOnClickListener(view -> {
                if( callback != null){
                    callback.onItemClick(assets.get(getAdapterPosition()));
                }
            });
            parentLayout.setOnLongClickListener(v -> {
                if( callback == null){
                    return false;
                }
                callback.onItemLongClick(assets.get(getAdapterPosition()));
                return true;
            });
        }
    }

}
