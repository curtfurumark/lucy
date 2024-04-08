package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import se.curtrune.lucy.R;

public class SwipeAbleCardsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipeable_cards_activitiy);
        setTitle("swipe the sequence");
        log("SwipeAbleCardsActivity.onCreate(Bundle)");
    }
}