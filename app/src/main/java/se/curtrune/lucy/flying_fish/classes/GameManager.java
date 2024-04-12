package se.curtrune.lucy.flying_fish.classes;

import java.util.List;

public class GameManager {
    private static GameManager instance;
    private GameManager(){}
    public static GameManager getInstance(){
        if( instance == null){
            instance = new GameManager();
        }
        return instance;
    }
    public boolean collision(Fish fish, List<DrawAble> drawAbles){
        for(DrawAble drawAble: drawAbles){
            if( fish.getRect().intersect(drawAble.getRect())){

            }
        }
        return false;
    }
}
