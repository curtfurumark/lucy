package se.curtrune.lucy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import se.curtrune.lucy.dialogs.BoostDialog;

public class LucindaViewModel extends ViewModel {
    private final MutableLiveData<Integer> energy = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateEnergy = new MutableLiveData<>();
    public void setEnergy(Integer energy){
        this.energy.setValue(energy);
    }
    public void updateEnergy(Boolean update){
        updateEnergy.setValue(update);
    }
    public LiveData<Integer>getEnergy(){
        return energy;
    }
    public LiveData<Boolean> getUpdateEnergy(){
        return updateEnergy;
    }
}
