package se.curtrune.lucy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import se.curtrune.lucy.classes.Item;

public class LucindaViewModel extends ViewModel {
    private final MutableLiveData<Integer> energy = new MutableLiveData<>();
    private final MutableLiveData<Item> currentParent =new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateEnergy = new MutableLiveData<>();
    public void setCurrentParent(Item item){
        currentParent.setValue(item);
    }

    /**
     * basically a hack, set energy to whatever you want, value does not matter
     * value will trigger update of mentalStats, for interested parties
     * @param energy
     */
    public void setEnergy(Integer energy){
        this.energy.setValue(energy);
    }
    public void updateEnergy(Boolean update){
        updateEnergy.setValue(update);
    }
    public LiveData<Item> getCurrentParent(){
        return currentParent;
    }
    public LiveData<Integer>getEnergy(){
        return energy;
    }
    public LiveData<Boolean> getUpdateEnergy(){
        return updateEnergy;
    }
}
