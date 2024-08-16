package se.curtrune.lucy.classes;

public class Action {


    public enum Type{
        NOTIFICATION, CATEGORY, REPEAT, TIME, EVENT, DATE, DURATION, MENTAL, TAGS, COLOR
    }
    private Type type;
    private String title;
    private String value;
    private int color = Integer.MIN_VALUE;
    public Action(){


    }

    public Action(String title){
        this.title = title;

    }
    public int getColor(){
        return color;
    }
    public String getTitle() {
        return title;
    }
    public Type getType() {
        return type;
    }
    public String getValue(){
        return value;
    }
    public boolean hasColor(){
        return color != Integer.MIN_VALUE;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setValue(String value){
        this.value = value;
    }
    public void setType(Type type){
        this.type = type;
    }
}
