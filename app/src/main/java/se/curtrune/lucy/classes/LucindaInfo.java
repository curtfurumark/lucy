package se.curtrune.lucy.classes;

public class LucindaInfo {
    String key;
    String value;
    public LucindaInfo(){

    }

    public LucindaInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public void setValue(int value){
        this.value = String.valueOf(value);
    }
}
