package se.curtrune.lucy.flying_fish.classes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import se.curtrune.lucy.R;


public class Hearts extends DrawAble{
    private Bitmap  redHeart;
    private Bitmap greyHeart;
    private int numHearts = 3;
    private Bitmap[] hearts;

    public Hearts(Resources resources){
        redHeart = BitmapFactory.decodeResource(resources, R.drawable.hearts);
        greyHeart = BitmapFactory.decodeResource(resources, R.drawable.heart_grey);
        hearts = new Bitmap[numHearts];
        for (int i = 0; i < numHearts; i++){
            hearts[i] = redHeart;
        }
        y = 30;

    }
    @Override
    public void draw(Canvas canvas) {
        for( int i = 0; i < numHearts; i++){
            x = (int) (580  + hearts[0].getWidth() * 1.5 * i);
            canvas.drawBitmap(hearts[i], x, y , null);
        }

    }

    @Override
    public void moveVertical(int i) {

    }
    public void setLifes(int lifes){
        hearts[lifes] = greyHeart;

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
