package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.MentalsToGraph;
import se.curtrune.lucy.workers.MentalWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyGraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyGraphFragment extends Fragment {


    private GraphView graphView;
    public static boolean VERBOSE = false;

    private TextView textViewDate;
    private Mental.Type currentMentalType = Mental.Type.ENERGY;
    // TODO: Rename and change types of parameters
    private LocalDate currentDate;

    private List<Mental> mentals;
    private MentalsToGraph mentalsToGraph;

    public DailyGraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment DailyGraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyGraphFragment newInstance() {
        return new DailyGraphFragment();
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
        log("DailyGraphFragment(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.graph_activity, container, false);
        initComponents(view);
        initListeners();
        currentDate = LocalDate.now();
        //radioButtonEnergy.setChecked(true);
        initGraph();
        initMentals(currentDate);
        initMentalsToGraph(currentDate);
        setUserInterface();
        //setUserInterface(LocalDate.now(), Mental.Type.ENERGY);
        /*initLineSeries();
        setUserInterface();
        //setUserInterface(currentDate, Mental.Type.ENERGY);
        getHourLabels();
        quantize();*/
        return view;
    }
    private void addSeriesToGraphView(DataPoint[] dataPoints, int color, String title){
        log("...addSeriesToGraphView(DataPoint[], int color, String title)");
        LineGraphSeries<DataPoint> dataPointSeries = new LineGraphSeries<>(dataPoints);
        //log("...number of points", dataPointSeries.)
        dataPointSeries.setColor(color);
        dataPointSeries.setTitle(title);
        graphView.addSeries(dataPointSeries);
        //double maxX = graphView.getViewport().getMaxX(true);
        //log("maxX", maxX);
    }
    private void chooseDate(){
        log("...chooseDate()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            log("...onDateSet(DatePicker, year, month, dayOfMonth");
            currentDate = LocalDate.of(year, month +1, dayOfMonth);
            log("...date chosen", currentDate);
            graphView.removeAllSeries();
            textViewDate.setText(currentDate.toString());
            //Toast.makeText(getContext(), "not implemented", Toast.LENGTH_LONG).show();
            initMentals(currentDate);
            initMentalsToGraph(currentDate);
            //setUserInterface();
        });
        datePickerDialog.show();
    }
    private DataPoint[] getDataPoints(Mental.Type mentalType){
        log("...getDataPoints(Mental.Type)", mentalType.toString());
        DataPoint[] dataPoints = new DataPoint[mentals.size()];
        int mentalLevel = 0;
        for(int i = 0; i < mentals.size(); i++){
            switch (mentalType){
                case ENERGY:
                    mentalLevel +=  mentals.get(i).getEnergy();
                    break;
                case STRESS:
                    mentalLevel +=  mentals.get(i).getStress();
                    break;
                case ANXIETY:
                    mentalLevel +=  mentals.get(i).getAnxiety();
                    break;
                case MOOD:
                    mentalLevel +=  mentals.get(i).getMood();
                    break;
            }
            dataPoints[i]  = new DataPoint(i, mentalLevel);
            //System.out.printf("DataPoint(%d, %d) );
        }
        return dataPoints;
    }
    private String[] getHourLabels(){
        log("...getHourLabels()");
        int hour = 0;
        int  now = LocalTime.now().getHour();
        String[] labels = new String[now + 1];
        int index = 0;
        while (hour <= now){
            labels[index++] = String.format(Locale.getDefault(),"%d", hour++);
        }
        return labels;
    }
    private void initComponents(View view){
        if( VERBOSE)log("...initComponents()");
        graphView = view.findViewById(R.id.graphActivity_graphView);
        textViewDate = view.findViewById(R.id.graphFragment_date);
    }
    private void initGraph(){
        log("...initGraph()");
        graphView.setTitle("week");
        graphView.setTitleColor(R.color.purple_200);
        graphView.setTitleTextSize(18);
        graphView.removeAllSeries();
        graphView.getViewport().setMinY(-8);
        graphView.getViewport().setMaxY(5);
        graphView.getViewport().setMinX(0);
        //graphView.getViewport().setMaxX(24);
        graphView.getViewport().setMaxX(LocalTime.now().getHour());
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(getHourLabels());
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }
    private void initGraph(int minX, int maxX, String[] labels){
        log("...initGraph(int, int , String[]");
        log("\t\tminX", minX);
        log("\t\tmaxX", maxX);
        for(int i = 0; i < labels.length; i++){
            log("hour", labels[i]);
        }
        graphView.setTitle(currentMentalType.toString());
        graphView.setTitleTextSize(56);
        graphView.removeAllSeries();
        graphView.getViewport().setMinY(-8);
        graphView.getViewport().setMaxY(5);
        graphView.getViewport().setMinX(minX);
        graphView.getViewport().setMaxX(maxX);
        //graphView.getViewport().setMaxX(Math.max(dataPoints.length, 12));
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        textViewDate.setText(currentDate.toString());
    }
    private void initLineSeries(){
        log("...initLineSeries()");
        DataPoint[] dataPointsEnergy = getDataPoints(Mental.Type.ENERGY);
        addSeriesToGraphView(dataPointsEnergy, Settings.getMentalColour(Mental.Type.ENERGY), getString(R.string.energy));
        DataPoint[] dataPointsMood = getDataPoints(Mental.Type.MOOD);
        addSeriesToGraphView(dataPointsMood, Settings.getMentalColour(Mental.Type.MOOD), getString(R.string.mood));
        DataPoint[] dataPointsAnxiety = getDataPoints(Mental.Type.ANXIETY);
        addSeriesToGraphView(dataPointsAnxiety, Settings.getMentalColour(Mental.Type.ANXIETY), getString(R.string.anxiety));
        DataPoint[] dataPointsStress = getDataPoints(Mental.Type.STRESS);
        addSeriesToGraphView(dataPointsStress, Settings.getMentalColour(Mental.Type.STRESS), getString(R.string.stress));
    }
    private void initListeners(){
        log("...initListeners()");
        textViewDate.setOnClickListener(view->chooseDate());
    }
    private void initMentals(LocalDate currentDate){
        log("...initMentals()");
        mentals = MentalWorker.getMentals(currentDate, false, true, getContext());
        mentals.forEach(System.out::println);
    }
    private void initMentalsToGraph(LocalDate date){
        log("...initMentalsToGraph()");
        mentalsToGraph = new MentalsToGraph(date, mentals);
        graphView.getViewport().setMaxX(mentalsToGraph.getLastHour());
        int lastHour = mentalsToGraph.getLastHour();
        int firstHour = mentalsToGraph.getFirstHour();
        String[] xLabels = mentalsToGraph.getXLabels();
        // initGraph(firstHour, lastHour, xLabels);
        DataPoint[] energyDataPoints = mentalsToGraph.getDataPoints(Mental.Type.ENERGY);
        addSeriesToGraphView(energyDataPoints, Settings.getMentalColour(Mental.Type.ENERGY), "energy");
        DataPoint[] anxietyDataPoints = mentalsToGraph.getDataPoints(Mental.Type.ANXIETY);
        addSeriesToGraphView(anxietyDataPoints, Settings.getMentalColour(Mental.Type.ANXIETY), "anxiety");
        DataPoint[] moodDataPoints = mentalsToGraph.getDataPoints(Mental.Type.MOOD);
        addSeriesToGraphView(moodDataPoints, Settings.getMentalColour(Mental.Type.MOOD), "mood");
        DataPoint[] stressDataPoints = mentalsToGraph.getDataPoints(Mental.Type.STRESS);
        addSeriesToGraphView(stressDataPoints, Settings.getMentalColour(Mental.Type.STRESS), "stress");
        log("...number of dataPoints", energyDataPoints.length);
/*        for(int i = 0; i < energyDataPoints.length; i++){
            log("...inside loop, hour", i);
            log(energyDataPoints[i]);
            //System.out.printf("hour %d, x:%.2f level %.2f\n", i, energyDataPoints[i].getX(), energyDataPoints[i].getY());
        }*/
    }



    private boolean isHour(LocalTime mentalTime, int  hour){
        return mentalTime.getHour() == hour;
    }
    private void quantize(){
        log("...quantize()");
        Map<Integer, List<Mental>> mentalMap = new TreeMap<>();
        int hour = LocalTime.now().getHour();
        for( int i = 0; i < hour; i++) {
            log("...currentHour", i);
            int finalI = i;
            List<Mental> filtered =  mentals.stream().filter(mental -> isHour(mental.getTime(), finalI)).collect(Collectors.toList());
            filtered.forEach(System.out::println);
            mentalMap.put(i, filtered);
        }
        for(Integer hour2: mentalMap.keySet()){
            System.out.printf("hour: %0d level", mentalMap.get(hour2).stream().mapToInt(Mental::getEnergy));
        }
    }
    private void setUserInterface(){
        textViewDate.setText(currentDate.toString());
    }

    private void setUserInterface(LocalDate date, Mental.Type mentalType){
        log("...setUserInterface(LocalDate, Mental.Type) ", date);
        List<Mental> mentals = MentalWorker.getMentals(date, false, true, getContext());
        DataPoint[] dataPoints = MentalWorker.getMentalsAsDataPoints(date,mentalType,  getContext());

        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoints);
        lineGraphSeries.setOnDataPointTapListener((series, dataPoint) -> {
            log("..onTap(Series, DataPointInterface)", dataPoint.toString());
            Mental mental = mentals.get((int) dataPoint.getX());
            Toast.makeText(getContext(), mental.getHeading(), Toast.LENGTH_LONG).show();
        });
        try {
            graphView.setTitle(currentMentalType.toString());
            graphView.setTitleTextSize(56);
            graphView.removeAllSeries();
            graphView.addSeries(lineGraphSeries);
            graphView.getViewport().setMinY(-8);
            graphView.getViewport().setMaxY(5);
            graphView.getViewport().setMinX(0);
            graphView.getViewport().setMaxX(24);
            //graphView.getViewport().setMaxX(Math.max(dataPoints.length, 12));
            graphView.getViewport().setYAxisBoundsManual(true);
            graphView.getViewport().setXAxisBoundsManual(true);
            textViewDate.setText(date.toString());
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}