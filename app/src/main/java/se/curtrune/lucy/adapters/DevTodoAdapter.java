package se.curtrune.lucy.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.curtrune.lucy.classes.DevTodo;

public class DevTodoAdapter extends RecyclerView.Adapter<DevTodoAdapter.ViewHolder> {
    private List<DevTodo> todoList;

    public DevTodoAdapter(List<DevTodo> todoList) {
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
    public void setList(List<DevTodo> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
