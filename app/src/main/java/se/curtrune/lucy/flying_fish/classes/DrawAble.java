package se.curtrune.lucy.flying_fish.classes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import se.curtrune.lucy.flying_fish.util.FishConstants;

public abstract class DrawAble {
    protected int x;
    protected int y;
    protected int height;
    protected int width;
    protected Paint paint;
    protected Rect rect;
    protected int x_speed;
    protected int y_speed;
    protected float dx, dy;
    public abstract void draw(Canvas canvas);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        System.out.println("x = " + x);
        this.x = x;
    }
    public boolean contains(float x, float y){
        rect.contains((int) x, (int) y);
        return false;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        System.out.println("y = " + y);
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public void move() {
        System.out.println("drawable move x_speed " + x_speed);
        if( x < 0 ){
            x = FishConstants.SCREEN_WIDTH;
        }
        if( y >  FishConstants.SCREEN_HEIGHT){
            y = 0;
        }
        x += x_speed;
        y += y_speed;
    }

    public abstract void moveVertical(int i);

    public abstract void moveHorizontal(float i);

    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }

    public  void setSpeed(int x, int y){
        x_speed = x;
        y_speed = y;
    }

    public  float getXSpeed(){
        return x_speed;
    }
    public  float getYSpeed(){
        return y_speed;
    }
    public abstract void update();

    public abstract Rect getRect();
}
