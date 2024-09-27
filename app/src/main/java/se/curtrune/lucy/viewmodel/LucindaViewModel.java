package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import se.curtrune.lucy.classes.Item;

public class LucindaViewModel extends ViewModel {
    public MutableLiveData<RecyclerMode> getRecyclerMode() {
        return recyclerMode;
    }

    public void toggleRecyclerMode() {
        log("....toggleRecyclerMode()");

        RecyclerMode rm = recyclerMode.getValue();
        rm = rm.equals(RecyclerMode.DEFAULT) ? RecyclerMode.MENTAL_COLOURS: RecyclerMode.DEFAULT;
        log("...new recyclerMode ", rm.toString());
        recyclerMode.setValue(rm);
    }

    //private final MutableLiveData<Integer> energy = new MutableLiveData<>();
    public enum RecyclerMode{
        DEFAULT, MENTAL_COLOURS
    }
    private final MutableLiveData<RecyclerMode> recyclerMode = new MutableLiveData();
    private final MutableLiveData<Item> currentParent =new MutableLiveData<>();
    private final MutableLiveData<Fragment> currentFragment = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateEnergy = new MutableLiveData<>();
    public void setCurrentParent(Item item){
        currentParent.setValue(item);
    }


    public void updateEnergy(Boolean update){
        updateEnergy.setValue(update);
    }
    public void updateFragment(Fragment fragment){
        currentFragment.setValue(fragment);
    }
    public LiveData<Item> getCurrentParent(){
        return currentParent;
    }

    public LiveData<Boolean> updateEnergy(){
        return updateEnergy;
    }
    public LiveData<Fragment> getFragment(){
        return currentFragment;
    }
    public LiveData<Boolean> getUpdateEnergy(){
        return updateEnergy;
    }

    public void setRecyclerMode(RecyclerMode recyclerMode) {
        log("ViewModel.setRecyclerMode()");
        this.recyclerMode.setValue(recyclerMode);
    }
}
