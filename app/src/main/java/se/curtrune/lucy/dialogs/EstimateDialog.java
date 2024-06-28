package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.Duration;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.MentalEstimate;
import se.curtrune.lucy.util.Constants;


public class EstimateDialog extends BottomSheetDialogFragment {
    private TextView textViewEnergy;
    private SeekBar seekBarEnergy;
    private Button buttonDismiss;
    private TextView textViewHours;
    private TextView textViewMinutes;
    private TextView textViewSeconds;
    private Button buttonSave;
    private MentalEstimate estimate;

    public static boolean VERBOSE = false;
    private int energy;
    public  enum Mode{
        CREATE, EDIT
    }
    private Mode mode = Mode.CREATE;
    public interface Callback{
        void onEstimate(MentalEstimate estimate, Mode mode);
    }
    private Callback callback;
    public EstimateDialog(){
        if( VERBOSE) log("EstimateDialog()");
        this.estimate = new MentalEstimate();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(VERBOSE)log("EstimateDateDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.add_estimate_dialog, container, false);
        initComponents(view);
        initListeners();
        setUserInterface();
        return view;
    }
    private MentalEstimate getEstimate(){
        if( VERBOSE) log("...getEstimate()");
        estimate = new MentalEstimate();
        long hours = Integer.parseInt(textViewHours.getText().toString());
        long minutes = Integer.parseInt(textViewMinutes.getText().toString());
        long seconds = Integer.parseInt(textViewSeconds.getText().toString());
        Duration duration = Duration.ofHours(hours);
        duration = duration.plusMinutes(minutes);
        duration = duration.plusSeconds(seconds);
        estimate.setDuration(duration.getSeconds());
        estimate.setEnergy(seekBarEnergy.getProgress() - Constants.ENERGY_OFFSET);
        return estimate;
    }

    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View)");
        buttonSave = view.findViewById(R.id.estimateItemDialog_buttonSave);
        buttonDismiss = view.findViewById(R.id.estimateItemDialog_buttonDismiss);
        textViewHours = view.findViewById(R.id.estimateDialog_hours);
        textViewMinutes = view.findViewById(R.id.estimateDialog_minutes);
        textViewSeconds = view.findViewById(R.id.estimateDialog_seconds);
        textViewEnergy = view.findViewById(R.id.estimateItemDialog_labelEnergy);
        seekBarEnergy = view.findViewById(R.id.estimateItemDialog_energySeekbar);
    }

    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonDismiss.setOnClickListener(view1 -> dismiss());
        buttonSave.setOnClickListener(view->{
            callback.onEstimate(getEstimate(), mode);
            dismiss();
        });
        seekBarEnergy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                log("...onProgressChanged(SeekBar, int, boolean)", progress);
                energy = progress - Constants.ENERGY_OFFSET;
                updateUserInterface(energy);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //this.listener = (Callback) context;
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void setUserInterface(){
        if(VERBOSE )log("...setUserInterface()", mode.toString());
        if( mode.equals(Mode.EDIT)) {
            String strEnergyEstimate = String.format(Locale.getDefault(), "%s %d",getString(R.string.energy), estimate.getEnergy());
            textViewEnergy.setText(strEnergyEstimate);
            //String strDurationEstimate = String.format(Locale.getDefault(), "%d", estimate.getDuration());
            seekBarEnergy.setProgress(Constants.ENERGY_OFFSET);
        }else{
            String strEnergyEstimate = String.format(Locale.getDefault(), "%s %d",getString(R.string.energy), 0);
            textViewEnergy.setText(strEnergyEstimate);
        }
    }
    private void updateUserInterface(int energy){
        log("...updateUserInterface()");
        String labelEnergy = String.format(Locale.getDefault(), "%s %s",getString(R.string.energy), energy);
        textViewEnergy.setText(labelEnergy);
    }
}
