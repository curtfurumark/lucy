package se.curtrune.lucy.web;

public class VersionInfo {
    private String versionName;
    private int versionCode;

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}
