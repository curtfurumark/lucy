package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.Duration;

import se.curtrune.lucy.R;


public class DurationDialog extends BottomSheetDialogFragment {
    private EditText editTextHours;
    private EditText editTextMinutes;
    private EditText editTextSeconds;
    private Button buttonSave;
    private Button buttonDismiss;

    public interface Callback{
        void onDurationDialog(Duration duration);
    }

    private Callback listener;
    private enum Mode{
        CREATE, EDIT
    }
    public DurationDialog(){
        log("DurationDialog constructor");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.duration_dialog, container, false);
        initComponents(view);
        initListeners();
        return view;
    }
    private Duration getDuration(){
        log("...getDuration()");
        long hours = Integer.parseInt(editTextHours.getText().toString());
        long minutes = Integer.parseInt(editTextMinutes.getText().toString());
        long seconds = Integer.parseInt(editTextSeconds.getText().toString());
        Duration duration = Duration.ofHours(hours);
        duration = duration.plusMinutes(minutes);
        duration = duration.plusSeconds(seconds);
        return duration;

    }
    private void initComponents(View view){
        log("...initComponents(View)");
        editTextHours = view.findViewById(R.id.durationDialog_hours);
        editTextMinutes = view.findViewById(R.id.durationDialog_minutes);
        editTextSeconds = view.findViewById(R.id.durationDialog_seconds);
        buttonSave = view.findViewById(R.id.durationDialog_save);
        buttonDismiss = view.findViewById(R.id.durationDialog_dismiss);
        log("...buttonDismiss is null", buttonDismiss == null ? "true": "false");
    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            Duration duration = getDuration();
            listener.onDurationDialog(duration);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //this.listener = (Callback) context;
    }
    public void setCallback(Callback callback){
        this.listener = callback;
    }

}
