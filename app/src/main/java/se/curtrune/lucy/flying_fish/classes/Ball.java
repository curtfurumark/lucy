package se.curtrune.lucy.flying_fish.classes;

import static se.curtrune.lucy.flying_fish.util.FishLogger.log;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

import se.curtrune.lucy.flying_fish.util.FishConstants;

public class Ball extends DrawAble {
    private int color;
    private int radius;
    private Random random = new Random();

    public Ball(float dx, int color) {
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(false);
        this.dx = dx;
        this.y = random.nextInt(FishConstants.SCREEN_HEIGHT);
        this.x = FishConstants.SCREEN_WIDTH;
    }
    public Ball(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(false);
        this.dx = -15;
        this.y = random.nextInt(FishConstants.SCREEN_HEIGHT);
        this.x = FishConstants.SCREEN_WIDTH;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, 30, paint);
    }

    public int getRadius() {
        return radius;
    }




    @Override
    public void moveVertical(int i) {
        y += i;
    }

    @Override
    public void moveHorizontal(float x) {
        System.out.println("Ball.moveHorizontal(float)");
        if( this.x < 0){
            this.x = FishConstants.SCREEN_WIDTH;
            paint.setColor(Color.GREEN);
        }
        this.x += x;
    }

    @Override
    public void setSpeed(int x, int y) {
        this.x_speed =  x;
        this.y_speed = y;
    }

    @Override
    public void update() {
        x += dx;
        //yellow_x -= yellow_speed;
        if( x < 0){
            x = FishConstants.SCREEN_WIDTH - 21;
            y = random.nextInt(FishConstants.SCREEN_WIDTH);
        }
    }

    @Override
    public Rect getRect() {
        return null;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
        //Color.BLACK.
    }

    public void setStyle(Paint.Style style){
        paint.setStyle(style);
    }

    @Override
    public boolean contains(float x, float y) {
        System.out.println("contains x: " + x + ", y: " + y);
        rect = new Rect();
        //width = 50;
        //height = 50;
        rect.set((int)this.x, (int)this.y, (int) this.x + width, (int)this.y + height);
        log(rect);
        return rect.contains((int)x, (int) y);
        //return super.contains(x, y);
    }
    public void setRadius(int radius) {
        this.radius = radius;
        width = radius;
        height = radius;
    }
}
