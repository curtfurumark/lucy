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

import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Estimate;
import se.curtrune.lucy.util.Constants;


public class AddEstimateDialog extends BottomSheetDialogFragment {
    private TextView textViewEnergy;
    private SeekBar seekBarEnergy;
    private TextView textViewDuration;
    private Button buttonDismiss;
    private Button buttonSave;
    private Estimate estimate;

    private int energy;
    public  enum Mode{
        CREATE, EDIT
    }
    private Mode mode = Mode.CREATE;
    public interface Callback{
        void onEstimate(Estimate estimate, Mode mode);
    }
    private Callback callback;
    public AddEstimateDialog(){
        log("AddEstimateDialog()");
        this.estimate = new Estimate();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("EstimateDateDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.add_estimate_dialog, container, false);
        initComponents(view);
       //initDefaults();
        initListeners();
        setUserInterface();
        return view;
    }
    private Estimate getEstimate(){
        log("...getEstimate()");
        estimate.setDuration(Long.valueOf(textViewDuration.getText().toString()));
        estimate.setEnergy(Integer.valueOf(energy));
        return estimate;
    }

    private void initComponents(View view){
        log("...initComponents(View)");
        //editTextDays = view.findViewById(R.id.periodDialog_days);
        buttonSave = view.findViewById(R.id.estimateItemDialog_buttonSave);
        buttonDismiss = view.findViewById(R.id.estimateItemDialog_buttonDismiss);
        textViewDuration = view.findViewById(R.id.estimateItemDialog_duration);
        textViewEnergy = view.findViewById(R.id.estimateItemDialog_labelEnergy);
        seekBarEnergy = view.findViewById(R.id.estimateItemDialog_energySeekbar);
    }

    private void initListeners(){
        log("...initListeners()");
        buttonDismiss.setOnClickListener(view1 -> {
            log("...dismiss please");
            dismiss();
        });
        buttonSave.setOnClickListener(view->{
            log("...save please");
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
        log("...setUserInterface()", mode.toString());
        if( mode.equals(Mode.EDIT)) {
            String strEnergyEstimate = String.format(Locale.getDefault(), "%s %d",getString(R.string.energy), estimate.getEnergy());
            textViewEnergy.setText(strEnergyEstimate);
            String strDurationEstimate = String.format(Locale.getDefault(), "%d", estimate.getDuration());
            textViewDuration.setText(strDurationEstimate);
            seekBarEnergy.setProgress(Constants.ENERGY_OFFSET);
        }
    }
    private void updateUserInterface(int energy){
        log("...updateUserInterface()");
        String labelEnergy = String.format(Locale.getDefault(), "%s %s",getString(R.string.energy), energy);
        textViewEnergy.setText(labelEnergy);
    }
}
