package se.curtrune.lucy.activities.flying_fish.util;

import android.graphics.Bitmap;
import android.graphics.Rect;

import se.curtrune.lucy.activities.flying_fish.classes.DrawAble;
import se.curtrune.lucy.activities.flying_fish.classes.Fish;
import se.curtrune.lucy.util.Logger;


public class FishLogger extends Logger {
    public static void log(Fish fish){
        System.out.println("Debug.log(Fish)");
        System.out.println("...x = " + fish.getX() + ",y =  " + fish.getY());
        System.out.println("...height = " + fish.getHeight() +  ", width = " + fish.getWidth());
        //Debug.log(fish.getBitmap() ,"fish");
    }
    public static void log(Bitmap bitmap, String description){
        System.out.println("Debug.log(Bitmap)");
        System.out.println("...height: " + bitmap.getHeight());
        System.out.println("...width: " + bitmap.getWidth());
        //bitmap.

    }

    public static void log(DrawAble drawAble) {
        System.out.println("log(DrawAble()");
        System.out.println("...width " + drawAble.getWidth());
        System.out.println("...x_speed: " + drawAble.getXSpeed());
        System.out.println("...y_speed: " + drawAble.getYSpeed());
        System.out.println("...x: " + drawAble.getX());
        System.out.println("...y: " + drawAble.getY());
    }

    public static void log(Rect rect) {
        System.out.println("log(Rect)");
        System.out.println("....width: " + rect.width());
        System.out.println("....height: " + rect.height());
        System.out.println("....left: " + rect.left);
        System.out.println("....right: " + rect.right);
        System.out.println("....top: " + rect.top);
        System.out.println("....bottom: " + rect.bottom);
        //System.out.println("....flatten: " + rect.flattenToString());
    }

}
