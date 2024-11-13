package se.curtrune.lucy.classes;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Message {
    private String subject;
    private String content;
    private String category;
    private String user;
    private long id;
    private long created;

    public Message(){
        subject = content = user = "";
        this.created = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public Message(String subject, String content, String user) {
        this();
        this.subject = subject;
        this.content = content;
        this.user = user;
        this.created = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
    public String getCategory(){
        return category;
    }
    public String getContent(){
        return content;
    }
    public LocalDateTime getCreated(){
        return LocalDateTime.ofEpochSecond(created, 0, ZoneOffset.UTC);
    }
    public long getCreatedEpoch(){
        return created;
    }
    public String getSubject(){
        return subject;
    }
    public String getUser(){
        return user;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setCreated(LocalDateTime created){
        this.created = created.toEpochSecond(ZoneOffset.UTC);
    }
    public void setSubject(String subject){
        this.subject = subject;
    }
    public void setUser(String user){
        this.user = user;
    }

    public void setID(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", user='" + user + '\'' +
                ", id=" + id +
                ", created=" + created +
                '}';
    }
}
