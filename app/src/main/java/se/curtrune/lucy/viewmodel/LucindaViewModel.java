package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

import se.curtrune.lucy.adapters.MentalAdapter;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.Affirmation;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.workers.AffirmationWorker;
import se.curtrune.lucy.workers.MentalWorker;

public class LucindaViewModel extends ViewModel {
    public MutableLiveData<RecyclerMode> getRecyclerMode() {
        return recyclerMode;
    }
    private MutableLiveData<Integer> mutableEnergy = new MutableLiveData<>();
    private MutableLiveData<Integer> mutableAnxiety = new MutableLiveData<>();
    private MutableLiveData<Integer> mutableStress = new MutableLiveData<>();
    private MutableLiveData<Integer> mutableMood = new MutableLiveData<>();
    private MutableLiveData<Affirmation> mutableAffirmation = new MutableLiveData<>();
    private MutableLiveData<String> mutableFilter = new MutableLiveData<>();
    private int initialEnergy;
    private MentalAdapter.MentalType mentalType = MentalAdapter.MentalType.ENERGY;
    private int initialAnxiety;
    private int initialStress;
    private int initialMood;
    private Mental currentMental;

    private LocalDate date;
    public LiveData<Integer> getEnergy(){
        return mutableEnergy;
    }
    public LiveData<Integer> getAnxiety(){
        return mutableAnxiety;
    }
    public LiveData<Integer> getStress(){
        return mutableStress;
    }
    public LiveData<Integer> getMood(){
        return mutableMood;
    }

    public void init(LocalDate date, Context context) {
        log("LucindaViewModel.init(LocalDate)", date.toString());
        this.date = date;
        //initialEnergy = MentalWorker.getEnergy(date, context);
        currentMental = MentalWorker.getCurrentMental(date, context);
        switch (mentalType){
            case STRESS:
                mutableStress.setValue(currentMental.getStress());
                break;
            case ENERGY:
                mutableEnergy.setValue(currentMental.getEnergy());
                break;
            case MOOD:
                mutableMood.setValue(currentMental.getMood());
                break;
            case ANXIETY:
                mutableAnxiety.setValue(currentMental.getAnxiety());
                break;
        }

    }

    public void toggleRecyclerMode() {
        log("....toggleRecyclerMode()");
        RecyclerMode rm = recyclerMode.getValue();
        rm = rm.equals(RecyclerMode.DEFAULT) ? RecyclerMode.MENTAL_COLOURS: RecyclerMode.DEFAULT;
        log("...new recyclerMode ", rm.toString());
        recyclerMode.setValue(rm);
    }

/*    public void estimateAnxiety(int estimatedAnxiety, Context context) {
        log("...estimateAnxiety(int, Context)");
        int anxiety = MentalWorker.getAnxiety(date, context);
        mutableAnxiety.setValue(anxiety + estimatedAnxiety);
    }*/
    /**
     *
     * @param estimatedEnergy
     * @param context
     */
/*    public void estimateEnergy(int estimatedEnergy, Context context){
        log("...estimateEnergy(int)", estimatedEnergy);
        int currentEnergy = MentalWorker.getEnergy(LocalDate.now(), context);
        mutableEnergy.setValue(currentEnergy + estimatedEnergy);
    }*/
    public void estimateMood(int estimatedMood, Context context){
        log("...estimateMood(int, Context)", estimatedMood);
        //int currentMood = MentalWorker.getMood(date, context);
        //mutableMood.setValue(currentMood + estimatedMood);
    }
    public void estimateStress(int stress, Context context){
        log("LucindaViewModel.estimateStress(int, Context)", stress);
        //int currentStress = MentalWorker.getStress(date, context);
        //mutableStress.setValue(currentStress + stress);
    }
    public void filter(String query) {
        log("LucindaViewModel.filter(query)", query);
        mutableFilter.setValue(query);
    }
    public Settings.PanicAction getPanicAction(Context context){
        log("LucindaViewModel.getPanicAction()");
        return User.getPanicAction(context);

    }

    public void requestAffirmation() {
        log("LucindaViewModel.requestAffirmation()");
        AffirmationWorker.requestAffirmation(new AffirmationWorker.RequestAffirmationCallback() {
            @Override
            public void onRequest(Affirmation affirmation) {
                log("...onRequest(Affirmation)");
                mutableAffirmation.setValue(affirmation);
            }

            @Override
            public void onError(String message) {
                //Toast.makeText( MainActivity.this, message, Toast.LENGTH_LONG).setMentalType();
            }
        });
    }



    public LiveData<Affirmation> getAffirmation() {
        return mutableAffirmation;
    }
    public LiveData<String> getFilter(){
        return mutableFilter;
    }

    public void setCurrentEnergy(int energy) {
        log("MainViewModel.setCurrentEnergy(int)", energy);
        mutableEnergy.setValue(energy);
    }

    public void resetMental(Context context, MentalAdapter.MentalType type) {
        log("LucindaViewModel.resetMental()");
        this.mentalType = type;
        init(date, context);
    }

    public void setMentalType(MentalAdapter.MentalType mentalType) {
        log("LucindaViewModel.setMentalType(MentalType)", mentalType.toString());
        this.mentalType = mentalType;
        switch (mentalType){
            case STRESS:
                mutableStress.setValue(currentMental.getStress());
                break;
            case ANXIETY:
                mutableAnxiety.setValue(currentMental.getAnxiety());
                break;
            case ENERGY:
                mutableEnergy.setValue(currentMental.getEnergy());
                break;
            case MOOD:
                mutableMood.setValue(currentMental.getMood());
                break;
        }
    }


    //private final MutableLiveData<Integer> energy = new MutableLiveData<>();
    public enum RecyclerMode{
        DEFAULT, MENTAL_COLOURS
    }
    private final MutableLiveData<RecyclerMode> recyclerMode = new MutableLiveData();
    private final MutableLiveData<Item> currentParent =new MutableLiveData<>();
    private final MutableLiveData<Fragment> currentFragment = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateEnergy = new MutableLiveData<>();

    public void setEnergy(int energy){
        log("LucindaViewModel.setEnergy(int)", energy);
        mutableEnergy.setValue(energy);
    }

    public void updateEnergy(Boolean update){
        updateEnergy.setValue(update);
    }
    public void updateFragment(Fragment fragment){
        currentFragment.setValue(fragment);
    }

    public void updateEnergy(Context context){
        currentMental = MentalWorker.getCurrentMental(LocalDate.now(), context);
        mutableEnergy.setValue(currentMental.getEnergy());
    }
    public LiveData<Fragment> getFragment(){
        return currentFragment;
    }

    public void setAnxiety(int progress) {
        log("LucindaViewModel.setAnxiety(int)", progress);
        mutableAnxiety.setValue(progress);
    }
    public void setRecyclerMode(RecyclerMode recyclerMode) {
        log("ViewModel.setRecyclerMode()");
        this.recyclerMode.setValue(recyclerMode);
    }
}
