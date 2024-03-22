package se.curtrune.lucy.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;



public class GameActivity extends AppCompatActivity {
    private FlyingFishView flyingFishView;
    //private DevView devView;
    //private FlyingFishView
    private Handler handler = new Handler();
    private final static long INTERVAL = 60;
    private TextView textView_playGame;
    private TextView textView_dev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        flyingFishView = new FlyingFishView(this);
        //gameView = new GameView(this);
        setContentView(flyingFishView);
        //setContentView(gameView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
/*        Constants.SCREEN_HEIGHT = displayMetrics.heightPixels;
        Constants.SCREEN_WIDTH = displayMetrics.widthPixels;*/

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        flyingFishView.invalidate();
                    }
                });

            }
        }, 0, INTERVAL);
    }
}