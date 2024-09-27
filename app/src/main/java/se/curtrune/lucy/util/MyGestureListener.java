package se.curtrune.lucy.util;


import static se.curtrune.lucy.util.Logger.log;

import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onDown(@NonNull MotionEvent event) {
        log("MyGestureListener.onDown(MotionEvent)");
        return true;
    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        log("...onFling(...)");
        if( e1 == null){
            log("...e1 is null");
        }else {
            float startX = e1.getX();
            float startY = e1.getY();
            float endX = e2.getX();
            float endY = e1.getY();
            if (startX > endX) {
                log("...FLING LEFT");
            } else {
                log("...FLING RIGHT");
            }
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
