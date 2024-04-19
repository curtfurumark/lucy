package se.curtrune.lucy.adapters;



import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.economy.classes.Transaction;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder>{
    private List<Transaction> transactions;
    public boolean VERBOSE = true;
    public void setList(List<Transaction> items) {
        if( items == null){
            log("ERROR,, TransactionAdapter.setList(List<Transaction>) called with null items");
            return;
        }
        if( VERBOSE) log("TransactionAdapter.setList(List<Transaction>), size ", items.size());
        this.transactions = items;
        notifyDataSetChanged();
    }

    public interface Callback{
        void onItemClick(Transaction item);
        void onItemLongClick(Transaction item);
        //void onCheckboxClicked(Listable item, boolean checked);
    }
    private final Callback callback;

    public TransactionAdapter(List<Transaction> items, Callback callback) {
        if( VERBOSE) log("ItemsAdapter(List<Item>, Context, Callback) size", items.size());
        if (items == null){
            log("WARNING, taskList is null");
        }
        this.transactions = items;
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
        final Transaction transaction = transactions.get(position);;
        //TODO rename textView
        holder.textView_heading.setText(transaction.getHeading());
        holder.textView_info.setText(transaction.getInfo());
    }


    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView_heading;
        //private final CheckBox checkBox_state;
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
                    callback.onItemClick(transactions.get(getAdapterPosition()));
                }
            });
            parentLayout.setOnLongClickListener(v -> {
                if( callback == null){
                    return false;
                }
                callback.onItemLongClick(transactions.get(getAdapterPosition()));
                return true;
            });
        }
    }

}
