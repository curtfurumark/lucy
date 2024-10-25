package se.curtrune.lucy.item_settings;

public class ItemSetting {
    protected String heading;
    protected String value;
    protected ViewType viewType;
    protected boolean isChecked;

    public String getValue() {
        return value;
    }

    public enum ViewType{
        KEY_VALUE, CHECKBOX, UNDEFINED
    }
    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setName() {
    }

    public enum Key {
        TIME, DATE, DURATION, COLOR, REPEAT, NOTIFICATION, CATEGORY, TAGS, APPOINTMENT, DONE, TEMPLATE, PRIORITIZED, IS_CALENDAR_ITEM
    }
    Key key;
    public ItemSetting(){

    }
    public ItemSetting(String heading, Key key){
        this.heading = heading;
        this.key = key;
    }
    public String getHeading(){
        return heading;
    }

    public Key getKey(){
        return key;
    }
    public ViewType getViewType(){
        return viewType;
    }
    public boolean isChecked(){
        return isChecked;
    }
    public void setChecked(boolean checked){
        this.isChecked = checked;
    }
    public void setValue(String value){
        this.value = value;
    }
    public void setKey(){
        this.key = key;
    }
    @Override
    public String toString() {
        return "ItemSetting{" +
                "heading='" + heading  +
                '}';
    }
}
