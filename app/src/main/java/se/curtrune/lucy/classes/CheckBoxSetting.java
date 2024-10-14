package se.curtrune.lucy.classes;

public class CheckBoxSetting extends Setting{
    private boolean isChecked;
    public CheckBoxSetting(){
        super();
    }
    public CheckBoxSetting(String heading, boolean isChecked){
        this.heading = heading;
        this.isChecked = isChecked;
        this.viewType = ViewType.CHECKBOX;
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
