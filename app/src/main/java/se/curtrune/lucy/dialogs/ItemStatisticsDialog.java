package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.ItemStatistics;
import se.curtrune.lucy.util.Converter;

public class ItemStatisticsDialog extends BottomSheetDialogFragment {
    private ItemStatistics itemStatistics;
    private TextView textViewHeading;
    private TextView textViewTotalDuration;
    private TextView textViewAvgDuration;
    private TextView textViewAverageStress;
    private TextView textViewAverageEnergy;
    private TextView textViewAverageAnxiety;
    private TextView textViewAverageMood;
    private TextView textViewNumberOfOccasions;
    private Button buttonOK;
    public static boolean VERBOSE = false;
    public ItemStatisticsDialog(ItemStatistics statistics){
        log("ItemStatisticsDialog(ItemStatistics)");
        this.itemStatistics = statistics;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_statistics_dialog, container, false);
        initViews(view);
        initListeners();
        initUserInterface();
        return view;
    }
    private void initListeners(){
        if(VERBOSE)log("...initListeners()");
        buttonOK.setOnClickListener(view->dismiss());
    }
    private void initViews(View view){
        buttonOK = view.findViewById(R.id.itemStatisticsDialog_buttonOK);
        textViewHeading = view.findViewById(R.id.itemStatisticsDialog_heading);
        textViewTotalDuration = view.findViewById(R.id.itemStatisticsDialog_totalDuration);
        textViewAvgDuration = view.findViewById(R.id.itemStatisticsDialog_avgDuration);
        textViewAverageAnxiety = view.findViewById(R.id.itemStatisticsDialog_avgAnxiety);
        textViewAverageEnergy = view.findViewById(R.id.itemStatisticsDialog_averageEnergy);
        textViewAverageMood = view.findViewById(R.id.itemStatisticsDialog_avgMood);
        textViewAverageStress = view.findViewById(R.id.itemStatisticsDialog_avgStress);
        textViewNumberOfOccasions = view.findViewById(R.id.itemStatisticsDialog_numberOccasions);
    }
    private void initUserInterface(){
        log("...initUserInterface()");
        String totalDuration = String.format(Locale.getDefault(), "total duration: %s", Converter.formatSecondsWithHours(itemStatistics.getDuration()));
        String averageDuration = String.format(Locale.getDefault(), "average duration %s", Converter.formatSecondsWithHours(itemStatistics.getAverageDuration()));
        String averageAnxiety = String.format(Locale.getDefault(), "average anxiety %.1f", itemStatistics.getAverageAnxiety());
        String averageEnergy = String.format(Locale.getDefault(), "average energy %.1f", itemStatistics.getAverageEnergy());
        String averageMood = String.format(Locale.getDefault(), "average mood %.1f", itemStatistics.getAverageMood());
        String averageStress = String.format(Locale.getDefault(), "average stress %.1f", itemStatistics.getAverageStress());
        String numberOfOccasions = String.format(Locale.getDefault(), "number of occasions %d", itemStatistics.getNumberOfItems());
        textViewHeading.setText(itemStatistics.getHeading());
        textViewTotalDuration.setText(totalDuration);
        textViewAvgDuration.setText(averageDuration);
        textViewAverageAnxiety.setText(averageAnxiety);
        textViewAverageEnergy.setText(averageEnergy);
        textViewAverageMood.setText(averageMood);
        textViewAverageStress.setText(averageStress);
        textViewNumberOfOccasions.setText(numberOfOccasions);
    }
}
