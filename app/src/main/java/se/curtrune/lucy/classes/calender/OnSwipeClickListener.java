package se.curtrune.lucy.classes.calender;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OnSwipeClickListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;

    public interface Listener {
        void onSwipeRight();
        void onSwipeLeft();
        void onClick();
    }
    private Listener listener;
    public OnSwipeClickListener(Context context, Listener listener) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            listener.onClick();
            return super.onSingleTapUp(e);
        }
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        @Override
        public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            try{
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if( Math.abs(diffX) > Math.abs(diffY)){
                    if( Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                        if( diffX > 0){
                            log("swipe right");
                            listener.onSwipeRight();
                        }else{
                            log("swipe left");
                            listener.onSwipeLeft();
                        }
                    }
                }else{
                    if( Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                        if( diffY > 0){
                            onSwipeDown();
                        }else{
                            onSwipeUp();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
        public void onSwipeRight(){


        }
        public void onSwipeLeft(){
            log("OnSwipeListener.onSwipeLeft()");
        }
        public void onSwipeUp(){


        }
        public void onSwipeDown(){


        }
        public void onClick(){


        }
        public void onDoubleClick(){


        }
        public void onLongClick(){


        }
    }
}