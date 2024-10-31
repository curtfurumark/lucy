package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.util.List;

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
    private TextView textViewHeading;
    private LocalDate firstDate;
    private LocalDate lastDate;

    /**
     * the number of dates to setMentalType, counting backwards from today
     */
    private int numberOfDays = 7;
    public MentaHistoryFragment() {
        log("MentalHistoryFragment");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment DailyGraphFragment.
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
        initPeriodDefaults();
        initGraph();
        initMultipleLinesGraph();
        initListeners();
        setUserInterface();
        //setUserInterface(currentDate);
        return view;
    }

    private void addDataPointsToGraph(DataPoint[] dataPoints, int color, String description){
        log("...addDataPointsToGraph()");
        LineGraphSeries<DataPoint> listDataPoints = new LineGraphSeries<>(dataPoints);
        listDataPoints.setColor(color);
        listDataPoints.setTitle(description);
        graphView.addSeries(listDataPoints);

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
        datePickerDialog.setMentalType();
    }*/
    private void initComponents(View view){
        log("...initComponents(View)");
        graphView = view.findViewById(R.id.mentalHistoryFragment_graphView);
        textViewHeading = view.findViewById(R.id.mentalHistoryFragment_heading);
    }
    private void initPeriodDefaults(){
        log("...initPeriodDefaults()");
        lastDate = LocalDate.now();
        firstDate = lastDate.minusDays(numberOfDays -1);
    }
    private DataPoint[] getDataPoints(Mental.Type mentalType){
        log("...getDataPoints(Mental.Type)", mentalType.toString());
        int mentalLevel = 0;
        DataPoint[] dataPointsArray = new DataPoint[numberOfDays];
        //LocalDate currentDate = LocalDate.now().minusDays(numberOfDays);
        LocalDate currentDate = firstDate;
        for(int i = 0; i < numberOfDays; i++){
            Mental mental = MentalWorker.getMental(currentDate, getContext());
            switch (mentalType){
                case ENERGY:
                    mentalLevel = mental.getEnergy();
                    break;
                case MOOD:
                    mentalLevel = mental.getMood();
                    break;
                case STRESS:
                    mentalLevel = mental.getStress();
                    break;
                case ANXIETY:
                    mentalLevel = mental.getAnxiety();
                    break;
            }
            System.out.printf("date %s, level %d\n", currentDate.toString(), mentalLevel);
            dataPointsArray[i] = new DataPoint(i, mentalLevel);
            currentDate = currentDate.plusDays(1);
        }
        return dataPointsArray;
    }
    private void initGraph(){
        log("...initGraph()");
        graphView.setTitle("week");
        graphView.setTitleColor(R.color.purple_200);
        graphView.setTitleTextSize(18);
        graphView.removeAllSeries();
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(getHorizontalLabels(numberOfDays));
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }
    private void initGraph(DataPoint[] dataPoints){
        log("...initGraph(DataPoint[])");
        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoints);
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
        //graphView.addSeries(barGraphSeries);
        lineGraphSeries.setColor(Color.RED);
        graphView.addSeries(lineGraphSeries);
    }
    private void initMultipleLinesGraph(){
        log("...initMultipleLinesGraph");
        DataPoint[] dataPointsEnergy = getDataPoints(Mental.Type.ENERGY);
        addDataPointsToGraph(dataPointsEnergy, Color.RED, "energy");
        DataPoint[] dataPointsMood = getDataPoints(Mental.Type.MOOD);
        addDataPointsToGraph(dataPointsMood, Color.BLUE, "mood");
        DataPoint[] dataPointsStress = getDataPoints(Mental.Type.STRESS);
        addDataPointsToGraph(dataPointsStress, Color.CYAN, "stress");
        DataPoint[] dataPointsAnxiety = getDataPoints(Mental.Type.ANXIETY);
        addDataPointsToGraph( dataPointsAnxiety, Color.MAGENTA, "anxiety");
    }
    private void initListeners(){
        log("...initListeners()");
        //textViewDate.setOnClickListener(view->chooseDate());
    }
/*    private void onMentalCheckBox(Mental.Type mentalType){
        log("...onMentalCheckBox(Mental.Type", mentalType.toString());
        initGraph(getDataPoints(mentalType));
    }*/
    private void setUserInterface(){
        log("...setUserInterface()");
        String heading = String.format("%s - %s", firstDate.toString(), lastDate.toString());
        textViewHeading.setText(heading);
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