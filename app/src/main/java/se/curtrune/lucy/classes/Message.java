package se.curtrune.lucy.classes;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Message {
    private String subject;
    private String content;
    private String user;
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

}
