package se.curtrune.lucy.persist;

import java.util.Arrays;

public class DB1Result {
    private boolean status;
    private String sql;
    private String php_file;
    /**
     * for client side exception
     */
    private String exception;
    private String json;
    private long id;
    //private String[] log;
    private String[] messages;
    public DB1Result(){
        json = this.php_file = this.sql = "";
        status = false;

    }
    public DB1Result(Exception e) {
        this();
        this.exception = e.getMessage();
    }
    public void addJson(String json){
        this.json = json;
    }

    public boolean isOK(){
        return status;
    }

    public String  getException(){
        return exception;
    }
    public String getJson(){
        return json;
    }
    public String[] getMessages(){
        return messages;
    }
    public String getMessage(){
        return Arrays.toString(messages) ;
    }
    public long getID(){
        return id;
    }
    public String getSql(){
        return sql;
    }
    public boolean hasException(){
        return exception != null;
    }

    @Override
    public String toString() {
        return "DB1Result{" +
                "status=" + status +
                ", sql='" + sql + '\'' +
                ", id=" + id +
                ", php_file= " + php_file +
                ", exception=" + exception  +
                ", log=" + Arrays.toString(messages) +
                '}';
    }

    public String getPhpFile() {
        return php_file;
    }
}
