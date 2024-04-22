package se.curtrune.lucy.classes;

import com.google.gson.Gson;

import java.io.Serializable;

public class Reward implements Serializable {
    private String message;
    public enum Type{
        AFFIRMATION, USER_DEFINED, CONFETTI
    }
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    public String toJson(){
        return new Gson().toJson(this);
    }
}
