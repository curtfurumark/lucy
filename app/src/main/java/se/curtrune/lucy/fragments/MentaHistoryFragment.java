package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
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
        initGraph();
        //initListeners();
        //currentDate = LocalDate.now();
        //setUserInterface(currentDate);
        initData();
        return view;
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
    }
    private void initData(){
        log("...initData()");
        LocalDate lastDate = LocalDate.now();
        LocalDate currentDate = lastDate.minusDays(6);
        //List<Mental> mentals = MentalWorker.select(firstDate, lastDate, getContext());
        //MentalWorker.getMentals()
        //mentals.forEach(System.out::println);
        while(currentDate.isBefore(lastDate) || currentDate.equals(lastDate)){
            List<Mental> mentals = MentalWorker.getMentals(currentDate, false, true, getContext());
            int energy = MentalWorker.calculateEnergy(mentals);
            log(String.format(Locale.getDefault(), "date %s, energy %d", currentDate.toString(), energy));
            currentDate = currentDate.plusDays(1);
        }
    }
    private void initGraph(){
        log("...initGraph()");
        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 2),
                new DataPoint(2, 3),
                new DataPoint(3, 4),
                new DataPoint(4, 5),
                new DataPoint(5, 6)
        });

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

        // on below line we are adding
        // data series to our graph view.
        graphView.addSeries(barGraphSeries);
    }
    private void initListeners(){
        log("...initListeners()");
        //textViewDate.setOnClickListener(view->chooseDate());
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