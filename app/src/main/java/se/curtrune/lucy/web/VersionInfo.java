package se.curtrune.lucy.web;

public class VersionInfo {
    private String versionName;
    private String fileName;
    private int versionCode;
    private String versionInfo;
    private String url;


    public String getFileName(){
        return fileName;
    }
    public String getUrl(){
        return url;
    }
    public String getVersionInfo(){
        return versionInfo;
    }

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
