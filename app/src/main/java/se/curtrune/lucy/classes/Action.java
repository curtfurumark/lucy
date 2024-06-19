package se.curtrune.lucy.classes;

public class Action {
    public enum Type{
        NOTIFICATION, CATEGORY, REPEAT, TIME, EVENT, DATE, DURATION
    }
    private Type type;
    private String title;

    public String getTitle() {
        return title;
    }
    public Type getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setType(Type type){
        this.type = type;
    }
}
