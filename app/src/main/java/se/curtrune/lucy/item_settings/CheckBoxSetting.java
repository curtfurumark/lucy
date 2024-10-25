package se.curtrune.lucy.item_settings;

public class CheckBoxSetting extends ItemSetting {
    private boolean isChecked;
    public CheckBoxSetting(){
        super();
    }
    public CheckBoxSetting(String heading, boolean isChecked, Key key){
        this.heading = heading;
        this.isChecked = isChecked;
        this.viewType = ViewType.CHECKBOX;
        this.key = key;
    }
    public void setChecked(boolean checked){
        this.isChecked = checked;
    }
    public boolean isChecked(){
        return isChecked;
    }

    @Override
    public String toString() {
        return "CheckBoxSetting{" +
                "isChecked=" + isChecked +
                ", heading='" + heading + '\'' +
                ", viewType=" + viewType +
                '}';
    }
}
