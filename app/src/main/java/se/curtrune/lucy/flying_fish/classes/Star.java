package se.curtrune.lucy.flying_fish.classes;

import static se.curtrune.lucy.flying_fish.util.FishLogger.log;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;


public class Star extends DrawAble {
    private Bitmap bitmap;
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
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

    public void rotate(int degrees){
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        int x_center = bitmap.getWidth() / 2;
        int y_center = bitmap.getHeight()/ 2;
        x_center = y_center = -100;
        System.out.println("...x_center: " + x_center + ", y_center: " +  y_center);
        bitmap = Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        System.out.println("...new height: " + bitmap.getHeight() + ", new width " + bitmap.getWidth());
        log(bitmap, "rotated");
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        height = bitmap.getHeight();
        width = bitmap.getWidth();
    }
}
