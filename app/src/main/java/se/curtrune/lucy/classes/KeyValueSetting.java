package se.curtrune.lucy.classes;

public class KeyValueSetting extends Setting{
    private String key;
    private String value;
    private boolean isSet;

    public KeyValueSetting(String key, boolean isSet, Name name) {
        super(key, name);
        this.key = key;
        this.isSet = isSet;
        this.viewType = ViewType.KEY_VALUE;
    }


    public String getValue() {
        return value;
    }
}
