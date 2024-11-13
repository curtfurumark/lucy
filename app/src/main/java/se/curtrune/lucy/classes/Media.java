package se.curtrune.lucy.classes;

public class Media {
    private String filePath;
    private FileType fileType;

    public enum FileType{
        TEXT, URL, IMAGE_JPEG
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
