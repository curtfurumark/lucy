package se.curtrune.lucy.adapters;


import static se.curtrune.lucy.util.Logger.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.R;


public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.MyViewHolder>{
    private List<String> categories;
    public boolean VERBOSE = false;
    public void setList(List<String> categories) {
        if( categories == null){
            log("ERROR,, CategoriesAdapter.setList(List<Transaction>) called with null items");
            return;
        }
        if( VERBOSE) log("CategoriesAdapter.setList(List<Transaction>), size ", categories.size());
        this.categories = categories;
        notifyDataSetChanged();
    }

    public interface Callback{
        void onItemClick(String category);
    }
    private final Callback callback;

    public SimpleAdapter(List<String> categories, Callback callback) {
        if( VERBOSE) log("SimpleAdapter(List<String>,Callback)");
        if (categories == null){
            log("WARNING, taskList is null");
        }
        this.categories = categories;
        this.callback = callback;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        if( VERBOSE) log("SimpleAdapter.onCreateViewHolder(...)");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull final MyViewHolder holder, int position) {
        if( VERBOSE) log("SimpleAdapter.onBindViewHolder();");
        final String category = categories.get(position);
        if(VERBOSE) log("...category", category);
        holder.textViewCategory.setText(category);
    }
    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewCategory;
        //private final LinearLayout layout;

        public MyViewHolder(@androidx.annotation.NonNull View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.categoryAdapter_category);
            textViewCategory.setOnClickListener(view -> {
                if( callback != null){
                    callback.onItemClick(categories.get(getAdapterPosition()));
                }
            });
        }
    }

}
