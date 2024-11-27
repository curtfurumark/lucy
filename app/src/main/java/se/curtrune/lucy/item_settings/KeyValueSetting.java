package se.curtrune.lucy.item_settings;

public class KeyValueSetting extends ItemSetting {
    private String key;

    public KeyValueSetting(String key, String value, Key name) {
        super(key, name);
        this.key = key;
        this.value = value;
        this.viewType = ViewType.KEY_VALUE;
    }
    public KeyValueSetting(String key, long value, Key keyName){
        super(key, keyName);
        this.key = key;
        this.value = String.valueOf(value);
        this.viewType = ViewType.KEY_VALUE;

    }

    @Override
    public String toString() {
        return "KeyValueSetting{" +
                "heading='" + heading + '\'' +
                ", value='" + value + '\'' +
                ", viewType=" + viewType +
                ", key=" + key +
                '}';
    }
}
