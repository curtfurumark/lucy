package se.curtrune.lucy.classes;

public class Setting {
    protected String heading;
    protected ViewType viewType;

    public enum ViewType{
        KEY_VALUE, CHECKBOX, UNDEFINED
    }
    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setName() {
    }

    public enum Name{
        TIME, DATE, DURATION, COLOR, REPEAT, NOTIFICATION, CATEGORY, TAGS
    }
    private Name name;
    public Setting(){

    }
    public Setting(String heading, Name name){
        this.heading = heading;
        this.name = name;
    }
    public String getHeading(){
        return heading;
    }


    public ViewType getViewType(){
        return viewType;
    }
    @Override
    public String toString() {
        return "Setting{" +
                "heading='" + heading  +
                '}';
    }
}
