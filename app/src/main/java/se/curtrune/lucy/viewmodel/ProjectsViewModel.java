package se.curtrune.lucy.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.classes.Item;

public class ProjectsViewModel extends ViewModel {
    private Item currentParent;
    private Item root;
    private List<Item> stack = new ArrayList<>();

    public ProjectsViewModel(){
    }
    public Item getCurrentParent(){
        return currentParent;
    }
    public void setCurrentParent(Item item){
        this.currentParent = item;
    }
    public Item pop(){
        if( stack.size() < 1){
            return null;
        }
        return stack.remove(stack.size() - 1);
    }
    public void push(Item item) {
        currentParent = item;
        stack.add(item);
    }

    public List<Item> getStack() {
        return stack;
    }

    public void setRoot(Item root) {
        this.root = root;
        currentParent = root;
        stack.add(root);
    }
}
