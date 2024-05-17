package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import se.curtrune.lucy.R;

public class NotificationPopUp extends DialogFragment {
    private View source;
    public NotificationPopUp(View source){
        this.source = source;
    }
    public static NotificationPopUp newInstance(View view){
        return new NotificationPopUp(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.notification_popup, container);
        initComponents(view);
        return view;
    }
    private void initComponents(View view){


    }
    private void setPosition(){
        log("..setPosition()");

    }
}
