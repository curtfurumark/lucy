package se.curtrune.lucy.activities.flying_fish.classes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import se.curtrune.lucy.R;


public class Fish extends DrawAble {
    protected Bitmap[] bitmaps = new Bitmap[2];
    private int x_speed = 10;
    private boolean jump = false;
    private int speed = 0;
    private int bm_index = 0;
    public Fish(){

    }

    public Fish(Resources resources) {
        bitmaps[0] = BitmapFactory.decodeResource(resources, R.drawable.fish1);
        bitmaps[1] = BitmapFactory.decodeResource(resources, R.drawable.fish2);
        width = bitmaps[0].getWidth();
        height = bitmaps[0].getHeight();
        rect = new Rect(x,y, x + width, y + height);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmaps[bm_index], x, y, null);
        if( jump){
            jump = false;}
/*        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(rect, paint);*/
        //updateRect();
        /*
        if( x > Constants.SCREEN_WIDTH){
            x = 0 - bitmap.getWidth();
            y = new Random().nextInt(Constants.SCREEN_HEIGHT);
        }
        if( y > Constants.SCREEN_HEIGHT){
            y = 0;
        }
        canvas.drawBitmap(bitmap, x, y, null);

         */
    }

    public void jump(boolean jump) {
        bm_index = jump ? 1:0;
        y += -22;
    }
    @Override
    public void moveVertical(int distance) {
        y += distance;
    }

    @Override
    public void moveHorizontal(float i) {
        x += i;
    }
    public void moveHorizontal(){
        x += x_speed;
    }

    public void rotate(){
        System.out.println("width: " + width + ", height: " + height);
        Matrix matrix = new Matrix();
        matrix.postRotate(-25);
        //matrix.
        //bitmap = Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        //bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true );
    }
    @Override
    public void update() {
        //if( y < Constants.SCREEN_HEIGHT + height) {
            y += 10;
            updateRect();
        //}
    }
    private void updateRect(){
        rect.set(x, y, x + width, y + height);

    }

    @Override
    public Rect getRect() {
        return rect;
    }


    public void setXSpeed(int x_speed) {
        this.x_speed = x_speed;
    }
}
