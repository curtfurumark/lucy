package se.curtrune.lucy.activities.flying_fish;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import se.curtrune.lucy.activities.flying_fish.classes.Background;
import se.curtrune.lucy.activities.flying_fish.classes.Ball;
import se.curtrune.lucy.activities.flying_fish.classes.Fish;


public class FlyingFishView extends View {
    private Bitmap[] bitmap_fish = new Bitmap[2];
    private Bitmap bitmap_background;
    private Background background;
    private Fish fish;
    private Paint  paint_score = new Paint();
    private Bitmap[] bitmap_lifes = new Bitmap[2];


    private Ball ball = new Ball();

    private Paint yellowBall = new Paint();
    private Paint greenBall = new Paint();
    private Paint redBall = new Paint();

    private int canvas_width;
    private int canvas_height;
    private boolean touch = false;

    public FlyingFishView(Context context) {
        super(context);
        log("FlyingFishView(Context)");
        background = new Background(getResources());
        fish = new Fish(getResources());
    }

    public boolean hitBallChecker(int x, int y){
/*        if( fish.getRect().contains(x, y)){
            return true;
        }*/

        return false;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //log("...onDraw(Canvas)");
        background.draw(canvas);
        fish.update();
        fish.draw(canvas);
        ball.update();
        ball.draw(canvas);
        if( fish.contains(ball.getX(), ball.getY())){
            log("...collision");
            ball.explode();
        }
        /*

        /*int minFishY = bitmap_fish[0].getHeight();
        //int maxFishY = canvas_height - bitmap_fish[0].getHeight() * 3;
        fish_y = fish_y + speed;
        if( fish_y < minFishY){
            fish_y = minFishY;
        }
        if( fish_y > Constants.SCREEN_HEIGHT){
            fish_y = Constants.SCREEN_HEIGHT;
        }

         */
        //speed += 2;
        /*
        if( touch){
            canvas.drawBitmap(bitmap_fish[1], fish_x, fish_y, null);
            touch = false;
        }else{
            canvas.drawBitmap(bitmap_fish[0], fish_x, fish_y, null);
        }

         */
/*        yellowBall.update();
        yellowBall.draw(canvas);
        if(hitBallChecker(yellowBall.getX(), yellowBall.getY())){
            score += 10;
            yellowBall.setX(-100);
        }
        greenBall.update();
        greenBall.draw(canvas);
        if(hitBallChecker(greenBall.getX(), greenBall.getY())){
            score += 20;
            //fish.setY(0);
            fish_y = 0;
            greenBall.setX(-100);
        }
        redBall.update();
        redBall.draw(canvas);
        //red_x -= red_speed;
        if(hitBallChecker(redBall.getX(), redBall.getY())){
            lifes--;
            hearts.setLifes(lifes);
            redBall.setX(-100);
            if( lifes == 0){
                hearts.draw(canvas);
                SystemClock.sleep(2000);
                Intent intent = new Intent(getContext(), GameOverActivity.class);
                intent.putExtra(Constants.INTENT_SCORE, score);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(intent);
            }
        }
        hearts.draw(canvas);

        canvas.drawText("score: " + score, 20, 60, paint_score);*/

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if( event.getAction() == MotionEvent.ACTION_DOWN){
            fish.jump(true);
        }else{
            fish.jump(false);
        }
        return true;
    }
}
