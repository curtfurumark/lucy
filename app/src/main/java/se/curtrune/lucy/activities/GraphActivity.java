package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.workers.MentalWorker;

public class GraphActivity extends AppCompatActivity {

    private GraphView graphView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);
        log("GraphActivity.onCreate(Bundle of joy)");
        initComponents();
        initMentalGraph(LocalDate.now());
    }
    private void initComponents(){
        log("...initComponents()");
        graphView = findViewById(R.id.graphActivity_graphView);
    }

    private void initMentalGraph(LocalDate date){
        log("...initMentalGraph()");
        List<Mental> mentals = MentalWorker.getMentals(date, false, true, this);
        DataPoint[] dataPoints = MentalWorker.getMentalsAsDataPoints(date, this);
        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoints);
        lineGraphSeries.setOnDataPointTapListener((series, dataPoint) -> {
            log("..onTap(Series, DataPointInterface)", dataPoint.toString());
            Mental mental = mentals.get((int) dataPoint.getX());
            Toast.makeText(this, mental.getHeading(), Toast.LENGTH_LONG).show();
        });
        graphView.setTitle(date.toString());
        graphView.setTitleTextSize(72);
        graphView.setTitleColor(R.color.purple_200);
        graphView.addSeries(lineGraphSeries);
        graphView.getViewport().setMinY(-8);
        graphView.getViewport().setMaxY(5);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(Math.max(dataPoints.length, 12));
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
    }
}