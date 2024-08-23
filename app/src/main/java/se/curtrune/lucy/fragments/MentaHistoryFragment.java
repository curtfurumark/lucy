package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.workers.MentalWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MentaHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MentaHistoryFragment extends Fragment {
    private GraphView graphView;
    //private TextView textViewDate;

    private RadioButton buttonEnergy;
    private RadioButton buttonStress;
    private RadioButton buttonAnxiety;
    private RadioButton buttonMood;
    private int numberOfDays = 7;
    public MentaHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MentaHistoryFragment newInstance() {
        return new MentaHistoryFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            log("...getArguments != null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("MentalHistoryFragment.onCreateView(LayoutInflater, ViewGroup, Bundle);");
        View view = inflater.inflate(R.layout.mental_history_fragment, container, false);
        initComponents(view);
        buttonEnergy.setChecked(true);
        initGraph(initData(Mental.Type.ENERGY));
        initListeners();
        //currentDate = LocalDate.now();
        //setUserInterface(currentDate);
        //initData();
        return view;
    }
    private String[] getHorizontalLabels(int numberOfDates){
        String[] labels = new String[numberOfDates];
        LocalDate currentDate = LocalDate.now().minusDays(numberOfDates -1);
        for(int i = 0; i < numberOfDates; i++){
            labels[i] = String.valueOf(currentDate.getDayOfMonth());
            currentDate = currentDate.plusDays(1);
        }
        return labels;
    }
/*    private void chooseDate(){
        log("...chooseDate()");
        DatePickerDialog dialog = new DatePickerDialog(getContext());
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                log("...onDateSet(DatePicker, year, month, dayOfMonth");
                currentDate = LocalDate.of(year, month +1, dayOfMonth);
                textViewDate.setText(currentDate.toString());
                setUserInterface(currentDate);
            }
        });
        datePickerDialog.show();
    }*/
    private void initComponents(View view){
        log("...initComponents()");
        graphView = view.findViewById(R.id.mentalHistoryFragment_graphView);
        //textViewDate = view.findViewById(R.id.graphFragment_date);
        buttonEnergy = view.findViewById(R.id.mentalHistoryFragment_buttonEnergy);
        buttonStress = view.findViewById(R.id.mentalHistoryFragment_buttonStress);
        buttonAnxiety = view.findViewById(R.id.mentalHistoryFragment_buttonAnxiety);
        buttonMood = view.findViewById(R.id.mentalHistoryFragment_buttonMood);
    }
    private DataPoint[] initData(Mental.Type mentalType){
        log("...initData(Mental.Type)", mentalType.toString());
        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>();
        int mentalLevel = 0;
        DataPoint[] dataPointsArray = new DataPoint[numberOfDays];
        LocalDate currentDate = LocalDate.now().minusDays(numberOfDays);
        for(int i = 0; i < numberOfDays; i++){
            switch (mentalType){
                case ENERGY:
                    mentalLevel = MentalWorker.getEnergy(currentDate, getContext());
                    break;
                case MOOD:
                    mentalLevel = MentalWorker.getMood(currentDate, getContext());
                    break;
                case STRESS:
                    mentalLevel = MentalWorker.getStress(currentDate, getContext());
                    break;
                case ANXIETY:
                    mentalLevel = MentalWorker.getAnxiety(currentDate, getContext());
                    break;
            }
            System.out.printf("date %s, level %d\n", currentDate.toString(), mentalLevel);
            dataPointsArray[i] = new DataPoint(i, mentalLevel);
            currentDate = currentDate.plusDays(1);
        }
        return dataPointsArray;
    }
    private void initGraph(DataPoint[] dataPoints){
        log("...initGraph(DataPoint[])");
        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(dataPoints);

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        graphView.setTitle("week");

        // on below line we are setting
        // text color to our graph view.
        graphView.setTitleColor(R.color.purple_200);

        // on below line we are setting
        // our title text size.
        graphView.setTitleTextSize(18);
        graphView.removeAllSeries();
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        //staticLabelsFormatter.setHorizontalLabels( new String[]{"17", "18", "19","20", "21", "22", "23"});
        staticLabelsFormatter.setHorizontalLabels(getHorizontalLabels(numberOfDays));
        // on below line we are adding
        // data series to our graph view.
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView.addSeries(barGraphSeries);
    }
    private void initListeners(){
        log("...initListeners()");
        //textViewDate.setOnClickListener(view->chooseDate());
        buttonMood.setOnClickListener(view->onMentalCheckBox(Mental.Type.MOOD));
        buttonAnxiety.setOnClickListener(view->onMentalCheckBox(Mental.Type.ANXIETY));
        buttonStress.setOnClickListener(view->onMentalCheckBox(Mental.Type.STRESS));
        buttonEnergy.setOnClickListener(view->onMentalCheckBox(Mental.Type.ENERGY));
    }
    private void onMentalCheckBox(Mental.Type mentalType){
        log("...onMentalCheckBox(Mental.Type", mentalType.toString());
        initGraph(initData(mentalType));
    }
    private void setUserInterface(LocalDate date){
        log("...setUserInterface(LocalDate) ", date);
        List<Mental> mentals = MentalWorker.getMentals(date, false, true, getContext());
        DataPoint[] dataPoints = MentalWorker.getMentalsAsDataPoints(date, getContext());
        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoints);
        lineGraphSeries.setOnDataPointTapListener((series, dataPoint) -> {
            log("..onTap(Series, DataPointInterface)", dataPoint.toString());
            Mental mental = mentals.get((int) dataPoint.getX());
            Toast.makeText(getContext(), mental.getHeading(), Toast.LENGTH_LONG).show();
        });
        graphView.setTitle("energy");
        graphView.setTitleTextSize(56);
        graphView.removeAllSeries();
        graphView.addSeries(lineGraphSeries);
        graphView.getViewport().setMinY(-8);
        graphView.getViewport().setMaxY(5);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(dataPoints.length > 12? dataPoints.length: 12);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        //textViewDate.setText(date.toString());
    }
}