package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.workers.MentalWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {


    private GraphView graphView;
    private RadioButton radioButtonStress;
    private RadioButton radioButtonAnxiety;
    private RadioButton radioButtonMood;
    private RadioButton radioButtonEnergy;

    private TextView textViewDate;
    private Mental.Type currentMentalType = Mental.Type.ENERGY;
    // TODO: Rename and change types of parameters
    private LocalDate currentDate;


    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment newInstance() {
        return new GraphFragment();

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
        View view = inflater.inflate(R.layout.graph_activity, container, false);
        initComponents(view);
        initListeners();
        currentDate = LocalDate.now();
        radioButtonEnergy.setChecked(true);
        setUserInterface(currentDate, Mental.Type.ENERGY);
        return view;
    }
    private void chooseDate(){
        log("...chooseDate()");
        DatePickerDialog dialog = new DatePickerDialog(getContext());
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                log("...onDateSet(DatePicker, year, month, dayOfMonth");
                currentDate = LocalDate.of(year, month +1, dayOfMonth);
                textViewDate.setText(currentDate.toString());
                setUserInterface(currentDate, currentMentalType);
            }
        });
        datePickerDialog.show();
    }
    private void initComponents(View view){
        log("...initComponents()");
        graphView = view.findViewById(R.id.graphActivity_graphView);
        textViewDate = view.findViewById(R.id.graphFragment_date);
        radioButtonStress = view.findViewById(R.id.graphFragment_radioButtonStress);
        radioButtonAnxiety = view.findViewById(R.id.graphFragment_radioButtonAnxiety);
        radioButtonEnergy = view.findViewById(R.id.graphFragment_radioButtonEnergy);
        radioButtonMood = view.findViewById(R.id.graphFragment_radioButtonMood);
    }
    private void initGraph(){
        log("...initGraph()");
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                // on below line we are adding
                // each point on our x and y axis.
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, -2),
                new DataPoint(4, 6),
                new DataPoint(5, 3),
                new DataPoint(6, 6),
                new DataPoint(7, 1),
                new DataPoint(8, 2)
        });

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        graphView.setTitle("energy");

        // on below line we are setting
        // text color to our graph view.
        graphView.setTitleColor(R.color.purple_200);

        // on below line we are setting
        // our title text size.
        graphView.setTitleTextSize(18);

        // on below line we are adding
        // data series to our graph view.
        graphView.addSeries(series);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewDate.setOnClickListener(view->chooseDate());
        radioButtonMood.setOnClickListener(view->onRadioButtonClick(Mental.Type.MOOD));
        radioButtonAnxiety.setOnClickListener(view->onRadioButtonClick(Mental.Type.ANXIETY));
        radioButtonStress.setOnClickListener(view->onRadioButtonClick(Mental.Type.STRESS));
        radioButtonEnergy.setOnClickListener(view->onRadioButtonClick(Mental.Type.ENERGY));
    }
    private void onRadioButtonClick(Mental.Type mentalType){
        log("...onRadioButtonClick(MentalType)", mentalType.toString());
        currentMentalType = mentalType;
        //Toast.makeText(getContext(), "radiobutton clicked", Toast.LENGTH_SHORT).show();
        setUserInterface(currentDate, currentMentalType);
    }
    private void setUserInterface(LocalDate date, Mental.Type mentalType){
        log("...setUserInterface(LocalDate) ", date);
        List<Mental> mentals = MentalWorker.getMentals(date, false, true, getContext());
        DataPoint[] dataPoints = MentalWorker.getMentalsAsDataPoints(date,mentalType,  getContext());
        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoints);
        lineGraphSeries.setOnDataPointTapListener((series, dataPoint) -> {
            log("..onTap(Series, DataPointInterface)", dataPoint.toString());
            Mental mental = mentals.get((int) dataPoint.getX());
            Toast.makeText(getContext(), mental.getHeading(), Toast.LENGTH_LONG).show();
        });
        graphView.setTitle(currentMentalType.toString());
        graphView.setTitleTextSize(56);
        graphView.removeAllSeries();
        graphView.addSeries(lineGraphSeries);
        graphView.getViewport().setMinY(-8);
        graphView.getViewport().setMaxY(5);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(dataPoints.length > 12? dataPoints.length: 12);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        textViewDate.setText(date.toString());
    }
}