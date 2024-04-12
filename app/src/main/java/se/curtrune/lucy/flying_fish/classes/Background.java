package se.curtrune.lucy.flying_fish.classes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import se.curtrune.lucy.R;


public class Background extends DrawAble{
    private Bitmap bitmap;
    public Background(Resources resources){
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.background);
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y , null);
    }

    @Override
    public void moveVertical(int i) {

    }

    @Override
    public void moveHorizontal(float i) {

    }

    @Override
    public void update() {

    }

    @Override
    public Rect getRect() {
        return null;
    }
}
