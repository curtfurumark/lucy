package se.curtrune.lucy.adapters;



import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.LogItem;
import se.curtrune.lucy.util.Converter;


public class LoggerAdapter extends RecyclerView.Adapter<LoggerAdapter.MyViewHolder>{
    private List<LogItem> logItems;
    public boolean VERBOSE = true;
    public void setList(List<LogItem> items) {
        if( items == null){
            log("ERROR,, LoggerAdapter.setList(List<Transaction>) called with null items");
            return;
        }
        if( VERBOSE) log("AssetsAdapter.setList(List<Transaction>), size ", items.size());
        this.logItems = items;
        notifyDataSetChanged();
    }

    public interface Callback{
        void onItemClick(LogItem logItem);
    }
    private final Callback callback;

    public LoggerAdapter(List<LogItem> items, Callback callback) {
        if( VERBOSE) log("ItemsAdapter(List<Item>, Context, Callback) size", items.size());
        if (items == null){
            log("WARNING, taskList is null");
        }
        this.logItems = items;
        this.callback = callback;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("ItemsAdapter.onCreateViewHolder(...)");
        android.view.View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.logger_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull final MyViewHolder holder, int position) {
        if( VERBOSE) log("ItemsAdapter.onBindViewHolder();");
        final LogItem logItem =  logItems.get(position);;
        //TODO rename textView
        holder.textViewMessage.setText(logItem.getMessage());
        holder.textViewTime.setText(Converter.format(logItem.getTime()));
    }


    @Override
    public int getItemCount() {
        return logItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewMessage;
        private final TextView textViewTime;
        private final LinearLayout layout;

        public MyViewHolder(@androidx.annotation.NonNull android.view.View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.loggerAdapter_message);
            textViewTime = itemView.findViewById(R.id.loggerAdapter_time);
            layout = itemView.findViewById(R.id.loggerAdapter_layout);
            layout.setOnClickListener(view -> {
                if( callback != null){
                    callback.onItemClick(logItems.get(getAdapterPosition()));
                }
            });
        }
    }

}
